# Cẩm nang làm việc với AI Agent

> Nguyên tắc gốc: **agent là đồng nghiệp làm nhanh nhưng mới vào team.** Giao việc rõ, cho đủ ngữ cảnh,
> kiểm tra kết quả. Người bấm merge chịu trách nhiệm cho code, dù ai viết.

## 1. Vòng làm việc chuẩn
```
Explore ──► Plan ──► Implement ──► Verify ──► Review (người) ──► Merge
   ▲          │ (>3 file: chờ duyệt)               │
   └──────────┴──────── sai hướng? quay lại ◄──────┘
```
| Bước | Bạn làm gì | Agent làm gì |
|---|---|---|
| Explore | Chỉ ra file/ticket/log liên quan | Đọc code, test, `docs/architecture.md`; **chưa sửa** |
| Plan | Đọc kế hoạch, sửa hướng, duyệt | Nêu mục tiêu, danh sách file, cách kiểm chứng |
| Implement | Theo dõi, ngắt sớm nếu lệch | Sửa từng bước nhỏ, theo skill nếu có |
| Verify | Đọc output lệnh thật | Chạy build + test, báo kết quả đúng sự thật |
| Review | Review diff như PR bình thường | (tuỳ chọn) chạy skill `code-review` tự soát trước |

Việc nhỏ (1–2 file, rõ ràng) có thể gộp Explore + Plan trong một câu.

## 2. Giao việc tốt
Một yêu cầu tốt có 4 phần — **Mục tiêu, Ngữ cảnh, Ràng buộc, Tiêu chí xong**:

```text
Mục tiêu: thêm GET /v1/payment-orders/{id}.
Ngữ cảnh: entity PaymentOrder đã có; xem PaymentOrderService.
Ràng buộc: không đổi schema DB; scope payments:read.
Xong khi: có OpenAPI, domain, use case + port, adapter, wiring, test 200/404, ./gradlew check xanh,
         pnpm --dir frontend typecheck vẫn xanh sau khi gen:api.
```

Nên:
- Dán **lỗi thật** (stack trace, log, output test) thay vì mô tả lại.
- Trỏ tới **ví dụ có sẵn** trong code: "làm giống `OrderController#create`".
- Chia việc lớn thành nhiều phiên nhỏ, mỗi phiên một mục tiêu kiểm chứng được.
- Nói rõ điều **không** được làm ("không đụng module billing").

Tránh:
- "Làm cho nó tốt hơn", "tối ưu đi" — không có tiêu chí xong.
- Gộp nhiều việc không liên quan trong một yêu cầu.
- Để agent tự chọn thư viện mới, thay đổi API công khai, hay migration mà không hỏi.

## 3. Dùng skill
Skill là playbook từng bước trong `.ai/skills/`. Gọi đích danh để agent làm đúng quy trình:

| Skill | Khi nào |
|---|---|
| `add-context` | Tạo bounded context mới (bộ module domain/application/adapter) |
| `add-endpoint` | Thêm API REST trong một context (contract-first), sinh lại type cho frontend |
| `add-page` | Thêm trang / form Next.js dùng API backend |
| `fix-bug` | Sửa lỗi: viết test tái hiện **trước**, rồi mới sửa |
| `code-review` | Review diff / MR theo rule của repo |
| `write-adr` | Ghi lại quyết định thiết kế |

Thấy mình lặp lại cùng một chuỗi hướng dẫn 3 lần → viết thành skill (xem
[maintaining-agent-config.md](maintaining-agent-config.md)).

## 4. DDD, Clean Architecture và agent
Backend chia module theo tầng; build tự chặn khi agent viết sai hướng phụ thuộc
([architecture.md](../architecture.md)). Khi agent báo `CleanArchitectureTest` đỏ:
- Đó là **tín hiệu thiết kế**, không phải test cần "sửa cho qua". Đọc rule nào vi phạm và đưa code về đúng tầng.
- Không chấp nhận diff nới lỏng `CleanArchitectureTest` hoặc thêm dependency Spring vào `shared`/`domain`/`application`.
- Soi kỹ mọi thứ agent thêm vào `modules/shared/*`: chỉ nhận code dùng chung thật (≥ 2 context/module), không logic nghiệp vụ.
- Giao việc theo tầng: "thêm use case X trong application, port ra Y, adapter Z" rõ hơn "thêm API X".
- Nói rõ **context** và dùng đúng **ngôn ngữ nghiệp vụ**: "trong context greeting, khách (`Guest`) được chào thì…".
  Agent hay đẩy logic vào use case/service — yêu cầu đặt quy tắc trong method của aggregate.
- Context này không được import domain của context khác; nếu agent cần, đó là dấu hiệu phải thêm `port.in` hoặc
  domain event, hoặc ranh giới context đang sai (ghi ADR).

## 5. Quản lý ngữ cảnh
- **Một phiên = một việc.** Xong việc thì mở phiên mới; phiên dài làm agent "quên" rule đầu phiên.
- Khi agent đi sai 2 lần liên tiếp: dừng, tóm tắt lại yêu cầu + điều đã học, bắt đầu phiên mới.
- Quy ước lặp lại nhiều lần → đưa vào rule trong `.ai/rules/`, đừng nhắc bằng miệng mỗi lần.
- Giữ `AGENTS.md` ngắn; chi tiết theo đường dẫn đưa vào rule có `globs`, quy trình đưa vào skill.

## 6. Review code do AI viết
Checklist khi review (bổ sung cho `.github/pull_request_template.md`):
- [ ] Hiểu được từng dòng — không merge thứ mình không giải thích được.
- [ ] Test **thực sự** kiểm tra hành vi (không chỉ assert `notNull`, không mock chính thứ đang test).
- [ ] Không có test bị xoá/nới lỏng, không `@Disabled` mới.
- [ ] Không thêm dependency, không gọi API/thư viện **không tồn tại** (AI hay "bịa" method).
- [ ] Code nằm đúng tầng và đúng context: quy tắc nghiệp vụ trong aggregate/value object, use case chỉ điều phối,
      controller chỉ gọi port, không framework trong lõi, không import chéo context.
- [ ] Xử lý lỗi, null, collection rỗng, transaction, concurrency.
- [ ] Không log dữ liệu nhạy cảm, không hard-code secret/URL môi trường.
- [ ] Diff gọn: không format lại file không liên quan, không code chết, không TODO vô chủ.

Dấu hiệu cần nghi ngờ: diff quá lớn so với yêu cầu, comment giải thích điều hiển nhiên, agent nói "đã test"
nhưng không có output lệnh.

## 7. Bảo mật
- Không dán secret, token, dữ liệu khách hàng thật, dump DB production vào prompt.
- Secret chỉ đi qua biến môi trường; MCP lấy qua `${env:...}` trong `.ai/mcp.json`.
- Chỉ bật MCP server tin cậy, quyền tối thiểu (token Jira chỉ đọc nếu đủ dùng).
- Nội dung agent đọc được (web, ticket, file log) có thể chứa **prompt injection** — đừng để agent tự
  làm hành động không thể hoàn tác (push, xoá, gửi tin) chỉ vì nội dung đó yêu cầu.
- Lệnh nguy hiểm bị chặn sẵn trong `.ai/permissions.json`; đừng nới trong file cá nhân nếu không hiểu rõ.

## 8. Chi phí & hiệu quả
- Ngữ cảnh rõ ngay từ đầu rẻ hơn nhiều vòng sửa sai.
- Dùng model nhỏ/nhanh cho việc tra cứu, tóm tắt; model mạnh cho thiết kế, debug khó.
- Chạy test đơn lẻ trong vòng lặp (`./gradlew :module:test --tests "*X"`, `pnpm --dir frontend test -- <file>`), cả suite ở bước Verify cuối.
- Việc chỉ đụng một stack thì nói rõ ("chỉ sửa frontend") để agent không đọc cả backend.

## 9. Khi nào **không** nên dùng agent
- Quyết định kiến trúc chưa có ai trong team hiểu vấn đề — dùng agent để khám phá phương án, không để quyết.
- Thay đổi trên môi trường dùng chung (DB migration, deploy, cấu hình prod).
- Code bạn không đủ khả năng review.

## 10. Câu hỏi thường gặp
**Agent không tuân theo rule?** Kiểm tra agent đã nạp file chưa (xem [getting-started.md](getting-started.md) §4),
rule có ngắn và cụ thể không, có rule nào mâu thuẫn không. Viết rule dạng mệnh lệnh, có ví dụ.

**Sửa trực tiếp `AGENTS.md` được không?** Không — nó là file sinh ra; sửa `.ai/rules/` rồi chạy sync.

**AI viết code, ghi credit thế nào?** Ghi trong mô tả PR (mục "AI assistance" của PR template). Commit
có thể thêm dòng `Co-Authored-By` theo chính sách team.
