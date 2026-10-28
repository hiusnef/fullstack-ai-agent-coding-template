# Bảo trì cấu hình agent

Tất cả cấu hình agent được viết **một lần** trong `.ai/`; `scripts/sync_agents.py` sinh ra file cho từng agent
([ADR 0001](../adr/0001-single-source-agent-config.md)). File sinh ra có header `GENERATED` và được commit.

```
.ai/
├── rules/NN-name.md       # rule: alwaysApply (gộp vào AGENTS.md) hoặc globs (rule theo đường dẫn)
├── skills/<name>/SKILL.md # playbook từng bước
├── agents/<agent>.md      # phần thêm riêng: claude, codex, copilot (codex.md được nối vào AGENTS.md)
├── mcp.json               # MCP server dùng chung
├── permissions.json       # allow/deny cho Claude Code (.claude/settings.json)
└── config.json            # targets: agent nào được sinh cấu hình
```

## Bật / tắt agent
Sửa `targets` trong `.ai/config.json` (hiện tại `["claude"]`), chạy `python scripts/sync_agents.py`.
Thư mục của agent bị tắt được xoá; bật lại thì được sinh lại từ `.ai/`. `AGENTS.md` luôn được sinh.

| Target | Sinh ra |
|---|---|
| `claude` | `CLAUDE.md`, `.claude/`, `.mcp.json` |
| `copilot` | `.github/copilot-instructions.md`, `.github/instructions/`, `.vscode/mcp.json`, `.vscode/settings.json` |
| `codex` | `.codex/config.toml`, `.agents/skills/` |
| `antigravity` | `.agents/rules/`, `.agents/skills/` |
| `cursor` | `.cursor/` |

Để đỡ rối: `.gitattributes` (sinh ra) đánh dấu mọi file sinh ra là `linguist-generated` nên GitHub thu gọn chúng
trong diff PR; khi bật `copilot`, `.vscode/settings.json` còn ẩn các thư mục chỉ là bản copy (`.claude/rules`, `.claude/skills`,
`.github/instructions`) khỏi Explorer và Search. Muốn sửa nội dung thì mở bản gốc trong `.ai/`.

## Thêm rule
1. Tạo `.ai/rules/NN-ten.md`. Số `NN` quyết định thứ tự trong `AGENTS.md`.
2. Frontmatter — chọn **một** trong hai:
   ```yaml
   ---
   description: Mô tả ngắn (hiện trong danh sách rule)
   alwaysApply: true
   ---
   ```
   ```yaml
   ---
   description: Mô tả ngắn
   globs: ["modules/contexts/*/adapter/persistence/**/*.java"]
   ---
   ```
3. `python scripts/sync_agents.py`, xem diff, commit cả `.ai/` lẫn file sinh ra.

Viết rule tốt: mệnh lệnh, cụ thể, kiểm chứng được ("Dùng AssertJ", không phải "viết test đẹp"); mỗi gạch đầu dòng
một ý; rule `alwaysApply` chỉ chứa điều áp dụng cho **mọi** việc. Giữ `AGENTS.md` dưới ~12k ký tự.

## Thêm skill
1. Tạo `.ai/skills/<ten>/SKILL.md`:
   ```yaml
   ---
   name: ten-skill
   description: Làm gì. Use when <tình huống kích hoạt>.
   ---
   ```
2. Thân skill: các bước đánh số, mỗi bước một hành động kiểm chứng được, bước cuối là tóm tắt kết quả.
3. File phụ (template, script) đặt cùng thư mục — generator copy cả thư mục.
4. Chạy sync. Thử: mở agent, yêu cầu một việc khớp `description`, xem agent có chọn đúng skill không.

`description` là thứ agent dùng để quyết định khi nào dùng skill — ghi rõ "Use when ...".

## Thêm / sửa MCP server
Sửa `.ai/mcp.json`. Secret chỉ đặt trong `env` dạng `${env:TEN_BIEN}`, không đặt trong `args`.
Generator đổi sang cú pháp riêng của từng agent (`${VAR}` cho Claude, `env_vars` cho Codex...).
Thêm biến mới vào `.env.example` và bảng trong [getting-started.md](getting-started.md).

## Đổi quyền Claude Code
Sửa `.ai/permissions.json` (cú pháp permission của Claude Code). `deny` luôn thắng `allow`.
Chỉ `allow` lệnh đọc hoặc lệnh build/test không có tác dụng phụ ra ngoài.

## Thêm agent mới
Thêm một khối trong `render()` của `scripts/sync_agents.py` trả về `{đường_dẫn: nội_dung}` và khai báo prefix
đường dẫn của agent trong `TARGETS`; generator tự lo manifest, xoá file cũ và `--check`. Cập nhật ma trận trong `README.md` và [getting-started.md](getting-started.md).

## CI báo "Agent config out of date"
Nguyên nhân thường gặp:
- Ai đó sửa tay file sinh ra (`AGENTS.md`, `CLAUDE.md`, `.claude/...`) → chuyển nội dung đó vào `.ai/`, chạy sync.
- Sửa `.ai/` nhưng quên chạy sync → chạy `python scripts/sync_agents.py` rồi commit.

Bật hook để khỏi quên: `git config core.hooksPath .githooks`.
