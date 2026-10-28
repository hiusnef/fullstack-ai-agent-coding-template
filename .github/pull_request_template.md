## What & why
<!-- Mục tiêu của thay đổi, link ticket -->

## How it was verified
<!-- Lệnh đã chạy và kết quả, ví dụ: ./gradlew check -> BUILD SUCCESSFUL; pnpm --dir frontend test -> passed -->

## AI assistance
- [ ] Không dùng AI
- [ ] Có dùng AI agent: <!-- tool/model, phần nào do agent viết -->

## Checklist
- [ ] Mỗi thay đổi hành vi có test; `./gradlew check` / `pnpm --dir frontend test` xanh
- [ ] Code đúng tầng Clean Architecture; không nới `CleanArchitectureTest`
- [ ] Đổi `api.yaml` thì đã chạy `gen:api` và sửa frontend trong cùng PR
- [ ] Tôi đã đọc và hiểu toàn bộ diff (kể cả phần AI viết)
- [ ] Không secret, không dependency mới (hoặc đã nêu lý do + licence)
- [ ] Không xoá / nới lỏng test có sẵn
- [ ] OpenAPI / docs / ADR đã cập nhật nếu cần
- [ ] Nếu sửa `.ai/`: đã chạy `python scripts/sync_agents.py` và commit file sinh ra
