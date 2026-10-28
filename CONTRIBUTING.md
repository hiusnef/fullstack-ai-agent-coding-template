# Đóng góp

## Quy trình
1. Tạo nhánh từ `main`: `feat/...`, `fix/...`, `docs/...`, `chore/...`.
2. Làm việc theo vòng **Explore → Plan → Implement → Verify** ([cẩm nang](docs/ai/handbook.md)) — áp dụng cho
   cả người lẫn AI agent.
3. Trước khi mở PR:
   ```bash
   ./gradlew spotlessApply && ./gradlew check                    # nếu đụng backend
   pnpm --dir frontend lint && pnpm --dir frontend typecheck && pnpm --dir frontend test   # nếu đụng frontend
   python scripts/sync_agents.py --check
   ```
4. Mở PR theo template; ghi rõ phần nào có AI hỗ trợ và cách đã kiểm chứng.

## Commit
Theo [Conventional Commits](https://www.conventionalcommits.org/): `feat: ...`, `fix: ...`, `docs: ...`.
Một commit = một thay đổi có nghĩa; không trộn format lại với thay đổi logic.

## Quy ước code
Nằm ở `AGENTS.md` (sinh từ `.ai/rules/`) — người và agent dùng chung một bộ quy tắc.
Muốn đổi quy ước: sửa `.ai/rules/`, chạy `python scripts/sync_agents.py`, nêu lý do trong PR
(quyết định lớn thì kèm ADR trong `docs/adr/`).

## Dùng AI agent
- Đọc [docs/ai/getting-started.md](docs/ai/getting-started.md) để cài đặt.
- Bạn chịu trách nhiệm toàn bộ code mình mở PR, kể cả phần agent viết.
- Không đưa secret, dữ liệu khách hàng thật vào prompt.
