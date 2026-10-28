# Bắt đầu

## 1. Yêu cầu
- **JDK 25** (Gradle không cần cài: dùng `./gradlew`), Docker (cho Testcontainers khi có test tích hợp).
- **Node.js ≥ 20.9** (khuyên dùng 24 LTS) và **pnpm** (`corepack enable` hoặc cài riêng).
- Python 3.10+ (chạy generator cấu hình agent, chỉ dùng stdlib).
- `npx` cho MCP `docs-fs`; [`uv`](https://docs.astral.sh/uv/) (`uvx`) cho MCP `atlassian`.
- [Claude Code](https://docs.claude.com/en/docs/claude-code) (agent đang dùng; agent khác xem §4).

## 2. Sau khi clone
```bash
git config core.hooksPath .githooks     # tự sync cấu hình agent khi commit thay đổi trong .ai/
python scripts/sync_agents.py --check   # phải in "Agent config up to date."
cp .env.example .env                    # rồi điền giá trị thật — .env đã được gitignore

./gradlew check                         # backend: build + test + ArchUnit + kiểm tra format
pnpm --dir frontend install             # theo pnpm-lock.yaml (CI dùng --frozen-lockfile)
pnpm --dir frontend typecheck           # sinh src/lib/api/schema.d.ts từ contract/api.yaml rồi kiểm tra type
pnpm --dir frontend test
```

Chạy local: `./gradlew :sample-service-bootstrap:bootRun --args='--spring.profiles.active=local'` (`:8080`) và `pnpm --dir frontend dev`
(`:3000`, gọi `/api/*` được chuyển sang backend). Mở http://localhost:3000 và thử form greeting.

Dùng SDKMAN / fnm: shell không tương tác (agent, script) có thể không nạp `~/.bashrc`, nên `java`/`node`
không có trong `PATH`. Khi đó đặt `JAVA_HOME=~/.sdkman/candidates/java/current` và thêm
`$JAVA_HOME/bin` cùng thư mục bin của Node vào `PATH` trước khi chạy lệnh.

## 3. Biến môi trường cho MCP
Các MCP server khai báo trong `.ai/mcp.json` lấy secret từ biến môi trường, **không** ghi trong file:

| Biến | Dùng cho | Ghi chú |
|---|---|---|
| `JIRA_URL` | `atlassian` | URL Jira của team |
| `JIRA_PERSONAL_TOKEN` | `atlassian` | Personal Access Token của chính bạn, quyền tối thiểu |

Export trong shell (hoặc dùng direnv / trình quản lý secret) **trước** khi mở agent, vì agent đọc env lúc khởi động.

## 4. Agent đang dùng: Claude Code
Repo hiện chỉ sinh cấu hình cho **Claude Code** (`targets` trong `.ai/config.json`). Claude đọc
`CLAUDE.md` → `AGENTS.md`, `.claude/rules`, `.claude/skills`, `.claude/settings.json` và `.mcp.json`.

Kiểm tra nhanh: chạy `claude`, gõ `/memory` để xem file hướng dẫn đã nạp, `/mcp` để xem MCP server.

Cần thêm agent khác (Copilot, Codex, Cursor, Antigravity) thì thêm vào `targets` và chạy sync — xem
[maintaining-agent-config.md](maintaining-agent-config.md#bật--tắt-agent) và ma trận trong `README.md`.

Lần đầu mở, agent sẽ hỏi có tin tưởng thư mục / cho phép MCP server không — đọc kỹ rồi mới đồng ý.

## 5. Tuỳ chỉnh cá nhân (không commit)
- `CLAUDE.local.md` — ghi chú riêng cho Claude Code.
- `.claude/settings.local.json` — quyền riêng của bạn (ví dụ cho phép thêm lệnh).

Không đưa quy ước của team vào file cá nhân; quy ước chung thuộc về `.ai/`.

## 6. Lưu ý về `.env.example`
File chỉ chứa **tên biến**, không có giá trị. Claude Code bị chặn đọc mọi `.env.*` (kể cả file này)
theo `.ai/permissions.json` — đó là chủ ý để an toàn; danh sách biến đã có ở bảng trên.
