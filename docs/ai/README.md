# Lập trình với AI Agent — bộ tài liệu

Tài liệu cho **người** làm việc cùng AI coding agent trong repo này. Phần dành cho **agent** nằm ở `.ai/`
(rule, skill) và được sinh ra thành `AGENTS.md`, `CLAUDE.md`…

| Tài liệu | Đọc khi |
|---|---|
| [getting-started.md](getting-started.md) | Lần đầu clone repo, cài agent, cấu hình MCP |
| [handbook.md](handbook.md) | **Cẩm nang**: quy trình làm việc hằng ngày, cách giao việc, review, bảo mật |
| [prompt-library.md](prompt-library.md) | Cần prompt mẫu cho việc thường gặp |
| [maintaining-agent-config.md](maintaining-agent-config.md) | Thêm/sửa rule, skill, MCP, agent mới; CI báo lệch cấu hình |

Quyết định thiết kế liên quan: [ADR 0001](../adr/0001-single-source-agent-config.md),
[ADR 0002](../adr/0002-agent-workflow-and-handbook.md), [ADR 0003](../adr/0003-generate-only-selected-agents.md),
[ADR 0004](../adr/0004-fullstack-layout-and-contract-codegen.md),
[ADR 0005](../adr/0005-clean-architecture-gradle-multi-module.md),
[ADR 0006](../adr/0006-modules-folder-and-naming.md),
[ADR 0007](../adr/0007-shared-kernel-module.md),
[ADR 0008](../adr/0008-ddd-bounded-context-modules.md),
[ADR 0009](../adr/0009-company-package-io-hiusnef.md).

## Tóm tắt 1 phút
1. Agent đọc `AGENTS.md` (Claude đọc `CLAUDE.md` → import `AGENTS.md`). Đó là "hợp đồng" giữa team và agent.
2. Mọi việc đi theo vòng **Explore → Plan → Implement → Verify** (`.ai/rules/05-agent-workflow.md`).
3. Việc lặp lại có **skill** sẵn: `add-context`, `add-endpoint`, `add-page`, `fix-bug`, `code-review`, `write-adr`.
4. Code do AI viết được review **như code của đồng nghiệp mới** — người merge chịu trách nhiệm.
5. Muốn đổi hành vi của agent: sửa trong `.ai/`, chạy `python scripts/sync_agents.py`, commit cả file sinh ra.
