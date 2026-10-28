# sample-service — fullstack Spring Boot 4 (DDD + Clean Architecture) + Next.js, làm việc cùng AI agent

**Stack:** Spring Boot 4.1 / Java 25, Gradle multi-module theo DDD (bounded context) × Clean Architecture · Next.js 16 / React 19 / TypeScript
(`frontend/`, pnpm) · contract OpenAPI `contract/api.yaml` → frontend sinh type tự động. Kiến trúc: [docs/architecture.md](docs/architecture.md).

```bash
./gradlew check                                                         # build + test + ArchUnit + format
./gradlew :sample-service-bootstrap:bootRun --args='--spring.profiles.active=local'   # backend :8080
pnpm --dir frontend install && pnpm --dir frontend dev    # frontend :3000 (proxy /api → :8080)
```

## Cấu hình AI agent

Một nguồn cấu hình duy nhất (`.ai/`) → script sinh ra file "bản địa" cho từng agent:
Claude Code, OpenAI Codex, GitHub Copilot, Google Antigravity, Cursor (và mọi tool đọc `AGENTS.md`).

> 📘 **Hướng dẫn & cẩm nang cho người dùng agent:** [docs/ai/](docs/ai/README.md) —
> [bắt đầu](docs/ai/getting-started.md) · [cẩm nang](docs/ai/handbook.md) · [prompt mẫu](docs/ai/prompt-library.md) ·
> [bảo trì cấu hình](docs/ai/maintaining-agent-config.md) · [đóng góp](CONTRIBUTING.md)

## Nguyên tắc

1. **Chỉ sửa trong `.ai/`.** Mọi file khác liên quan đến agent đều là file sinh ra (có header `GENERATED`).
2. **`AGENTS.md` là lõi** — chuẩn mở mà Codex, Copilot, Cursor, Antigravity đọc trực tiếp.
   Claude Code đọc `CLAUDE.md`, nên `CLAUDE.md` chỉ chứa `@AGENTS.md` + phần riêng cho Claude.
3. **Rule chia 2 loại:** `alwaysApply: true` → gộp vào `AGENTS.md`; `globs: [...]` → sinh ra rule theo đường dẫn
   cho agent hỗ trợ (Claude/Copilot/Cursor/Antigravity), còn trong `AGENTS.md` chỉ để link tới (Codex không có rule theo path).
4. **Skill** theo chuẩn Agent Skills (`SKILL.md`) → copy sang `.claude/skills/` và `.agents/skills/`.
5. **MCP** khai báo một lần trong `.ai/mcp.json` → render sang 4 định dạng khác nhau.
6. File sinh ra **được commit**, để clone về là dùng được; CI chặn khi bị lệch.
7. **Chỉ sinh cho agent team dùng:** danh sách trong `.ai/config.json` (hiện tại: chỉ `claude`).
   Agent không có trong danh sách thì thư mục của nó không tồn tại. File sinh ra được đánh dấu
   `linguist-generated` để PR thu gọn diff.

## Cấu trúc

```
.
├── .ai/                          # ⭐ SOURCE OF TRUTH — chỉ sửa ở đây
│   ├── rules/                    #   00-core, 05-agent-workflow, 10-clean-architecture, 30-security (always) | 11-java-style, 12-ddd, 15-frontend, 20-testing, 40-api (globs)
│   ├── skills/<name>/SKILL.md    #   playbook: add-context, add-endpoint, add-page, code-review, fix-bug, write-adr
│   ├── agents/<agent>.md         #   phần bổ sung riêng cho từng agent (claude, codex, copilot...)
│   ├── mcp.json                  #   MCP servers dùng chung
│   ├── permissions.json          #   allow/deny cho Claude Code
│   └── config.json               #   targets: agent nào được sinh cấu hình
├── scripts/sync_agents.py        # generator (Python stdlib, không cần cài gì)
│
│   # ── ứng dụng ──
├── settings.gradle.kts, gradle/libs.versions.toml, build-logic/   # Gradle multi-module + convention plugins
├── modules/                      # module Gradle: modules/<a>/<b> → :sample-service-<a>-<b> (tự phát hiện)
│   ├── shared/kernel/            #   lỗi (ErrorCode), building block DDD, tiện ích — chỉ JDK
│   ├── shared/web/               #   GlobalExceptionHandler dùng chung cho web adapter
│   ├── contexts/<ctx>/           #   mỗi bounded context (DDD) một thư mục, hiện có: greeting
│   │   ├── domain/               #     aggregate, value object, domain event, repository interface
│   │   ├── application/          #     use case + port.in (API công khai của context)
│   │   └── adapter/{web,persistence}/  # HTTP vào / cài đặt repository
│   └── bootstrap/                #   Spring Boot app, wiring, event publisher, test ArchUnit
├── contract/api.yaml             # OpenAPI dùng chung backend + frontend
├── frontend/                     # Next.js: src/app, src/components, src/lib/api (client + type sinh ra)
│
│   # ── sinh ra (với targets = claude) ──
├── AGENTS.md                     # lõi chung, luôn có
├── CLAUDE.md, .claude/, .mcp.json                                      # Claude Code
├── .gitattributes                # đánh dấu file sinh ra
│
├── .github/workflows/ci.yml             # CI: backend verify + frontend typecheck/lint/test/build
├── .github/workflows/agent-config.yml   # CI: sync_agents.py --check
├── .githooks/pre-commit                 # tự sync khi commit thay đổi trong .ai/
├── docs/  architecture.md, adr/ (0001–0009, template)
├── docs/ai/                             # hướng dẫn, cẩm nang, prompt mẫu cho người dùng agent
├── CONTRIBUTING.md, .github/pull_request_template.md, .editorconfig
```

## Ma trận agent → file
Bật agent nào thì thêm tên (`claude`, `copilot`, `codex`, `antigravity`, `cursor`) vào `.ai/config.json` rồi chạy sync.

| Agent | Instructions | Rule theo path | Skills | MCP |
|---|---|---|---|---|
| Claude Code | `CLAUDE.md` → `@AGENTS.md` | `.claude/rules/*.md` (`paths:`) | `.claude/skills/` | `.mcp.json` |
| Codex | `AGENTS.md` | — (link trong AGENTS.md) | `.agents/skills/` | `.codex/config.toml` |
| GitHub Copilot | `AGENTS.md` + `.github/copilot-instructions.md` | `.github/instructions/*.instructions.md` (`applyTo`) | (đọc `.claude/skills`) | `.vscode/mcp.json` |
| Antigravity | `AGENTS.md` | `.agents/rules/*.md` | `.agents/skills/` | cấu hình trong app |
| Cursor | `AGENTS.md` | `.cursor/rules/*.mdc` | — | `.cursor/mcp.json` |

## Cách dùng
```bash
python scripts/sync_agents.py          # sinh lại toàn bộ
python scripts/sync_agents.py --check  # dùng trong CI
python scripts/sync_agents.py --stage  # sinh + git add/rm file sinh ra (hook dùng)
git config core.hooksPath .githooks    # bật auto-sync khi commit
```

**Thêm rule:** tạo `.ai/rules/50-xxx.md` với frontmatter `description` + `alwaysApply: true` hoặc `globs: ["..."]` → chạy sync.
**Thêm skill:** tạo `.ai/skills/<ten>/SKILL.md` (frontmatter `name`, `description`) → chạy sync.
**Bật/tắt agent có sẵn:** sửa `targets` trong `.ai/config.json` → chạy sync (thư mục của agent bị tắt sẽ tự xoá).
**Thêm agent mới:** viết thêm một khối render trong `render()` và khai báo prefix đường dẫn trong `TARGETS` của `scripts/sync_agents.py`.

## Lưu ý
- Quy ước file của các agent thay đổi khá nhanh (Antigravity đã chuyển `.agent/` → `.agents/`, frontmatter
  activation của rule Antigravity cũng có thể khác bản bạn dùng). Kiểm tra docs của từng tool khi nâng cấp — chỉ cần sửa script.
- Giữ `AGENTS.md` ngắn (< ~12k ký tự — giới hạn mỗi file rule của Antigravity); chi tiết đưa vào rule theo path hoặc skill.
- Secret chỉ để trong `env` của `.ai/mcp.json` dưới dạng `${env:TEN_BIEN}`, không đặt trong `args`.
- Override cá nhân: `CLAUDE.local.md`, `.claude/settings.local.json` (đã gitignore).
