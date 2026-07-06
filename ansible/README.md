# LedgerFlow — Ansible deployment

Deploys **one** LedgerFlow Spring Boot service as a `systemd`-managed daemon on a
RHEL 8/9 or Rocky Linux 8/9 host. The play is idempotent: run it as often as you
like — nothing changes unless the jar, environment, or unit file actually change.

## What the `springboot-app` role does

1. Installs the **Eclipse Temurin 21 JDK** from the official Adoptium yum repo.
2. Creates a dedicated, non-login **system user and group** for the service.
3. Lays out `/opt/<app>`, `/etc/<app>`, `/var/log/<app>` with tight permissions.
4. Copies the locally built **fat jar** to the host.
5. Renders an **EnvironmentFile** (`/etc/<app>/<app>.env`) with port, profiles,
   JVM options and any extra environment (DB credentials, Kafka brokers, ...).
6. Installs a hardened **systemd unit**, then `enable`s and `start`s it.
7. Opens the service port in **firewalld** (permanent + immediate).

Changing the jar or the env/unit template triggers a handler that restarts the
service; unchanged runs report `ok` with no restart.

## Layout

```
ansible/
├── ansible.cfg
├── playbook.yml
├── requirements.yml
├── inventory.ini.example
├── group_vars/
│   └── ledgerflow.yml
├── roles/
│   └── springboot-app/
│       ├── defaults/main.yml
│       ├── handlers/main.yml
│       ├── meta/main.yml
│       ├── tasks/main.yml
│       └── templates/
│           ├── env.j2
│           └── springboot.service.j2
└── README.md
```

## Prerequisites

- Ansible 2.14+ on the control machine.
- SSH access to the target VM with a `sudo`-capable user.
- The service jar built locally, e.g.:

  ```bash
  mvn -B -f account-service/pom.xml clean package
  ```

- The `ansible.posix` collection (for the `firewalld` module):

  ```bash
  ansible-galaxy collection install -r ansible/requirements.yml
  ```

## Usage

```bash
cd ansible
cp inventory.ini.example inventory.ini      # edit host, user, DB vars
ansible-galaxy collection install -r requirements.yml
ansible-playbook playbook.yml
```

Dry run / see what would change:

```bash
ansible-playbook playbook.yml --check --diff
```

## Deploying a different service

`playbook.yml` ships configured for `account-service`. To deploy another service,
override the vars — either edit the `vars:` block or pass them on the CLI:

```bash
ansible-playbook playbook.yml \
  -e springboot_app_name=order-service \
  -e springboot_app_display_name="LedgerFlow order-service" \
  -e springboot_server_port=8082 \
  -e springboot_jar_src=../order-service/target/order-service-0.1.0-SNAPSHOT.jar \
  -e springboot_active_profiles=postgres \
  -e '{"springboot_environment": {"SPRING_KAFKA_BOOTSTRAP_SERVERS": "broker:9092"}}'
```

| Service | Default port |
|---|---|
| account-service | 8081 |
| order-service | 8082 |
| settlement-service | 8083 |
| notification-service | 8084 |

## Key variables (`roles/springboot-app/defaults/main.yml`)

| Variable | Default | Purpose |
|---|---|---|
| `springboot_app_name` | `ledgerflow-service` | Service/user/dir base name |
| `springboot_jar_src` | *(required)* | Local path to the built jar |
| `springboot_server_port` | `8080` | HTTP port; opened in firewalld |
| `springboot_active_profiles` | `""` | `SPRING_PROFILES_ACTIVE` (e.g. `postgres`) |
| `springboot_java_opts` | `-Xms256m -Xmx512m` | Passed as `$JAVA_OPTS` |
| `springboot_environment` | `{}` | Extra env (DB, Kafka, ...) |
| `springboot_open_firewall` | `true` | Manage firewalld |
| `springboot_java_home` | `/usr/lib/jvm/temurin-21-jdk` | JDK path in the unit |

## Notes

- The default `postgres` profile expects `DB_URL`, `DB_USER`, `DB_PASSWORD`
  (supplied via `springboot_environment` in `playbook.yml`). Drop the profile
  and those env vars to run against the built-in H2 database instead.
- Store real secrets with **Ansible Vault**, not in `inventory.ini`.
- Logs land in `/var/log/<app>/<app>.log`; inspect the unit with
  `journalctl -u <app>` and `systemctl status <app>`.
- The systemd unit is hardened (`NoNewPrivileges`, `PrivateTmp`,
  `ProtectSystem=full`, `ProtectHome`) with only the log dir writable.
