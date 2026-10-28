# Thư viện prompt mẫu

Thay phần trong `<...>`. Prompt có thể viết tiếng Việt hoặc tiếng Anh; tên file/class giữ nguyên.

## Tìm hiểu code (Explore)
```text
Chưa sửa gì. Đọc <package/thư mục> và giải thích: luồng xử lý chính của <tính năng>,
các class tham gia (file:line), chỗ nào có transaction, và các test đang cover nó.
```

## Lập kế hoạch
```text
Tôi cần <mục tiêu>. Đọc code liên quan rồi đề xuất kế hoạch: file sẽ sửa, thay đổi chính,
rủi ro, cách kiểm chứng. Chờ tôi duyệt rồi mới làm.
```

## Thêm bounded context
```text
Dùng skill add-context. Context <tên>: chịu trách nhiệm <một câu>. Thuật ngữ: <A, B, C>.
Quan hệ với context khác: gọi <ctx>.port.in.<X>UseCase / nghe sự kiện <Y>. Lập kế hoạch trước, chờ tôi duyệt.
```

## Thêm endpoint
```text
Dùng skill add-endpoint. <METHOD> /v1/<resource>.
Request: <fields + ràng buộc>. Response: <fields>. Lỗi: <404 khi..., 409 khi...>. Scope: <scope>.
```

## Thêm trang frontend
```text
Dùng skill add-page. Route /<path>. Người dùng thấy <...> và có thể <...>.
API dùng: <operationId hoặc METHOD path>. Trạng thái lỗi/rỗng: <...>.
```

## Một tính năng xuyên suốt BE + FE
```text
Tính năng: <mô tả>. Làm theo thứ tự: contract/api.yaml → skill add-endpoint (domain → use case + port → adapter → wiring)
→ gen:api → skill add-page.
Lập kế hoạch trước (đụng >3 file), chờ tôi duyệt.
```

## Sửa bug
```text
Dùng skill fix-bug. Kỳ vọng: <...>. Thực tế: <...>.
Cách tái hiện: <request/input>. Log/stack trace:
<dán nguyên văn>
```

## Viết test cho code có sẵn
```text
Viết unit test cho <Class#method> theo .ai/rules/20-testing.md. Cover: <case thường>, <biên>,
<lỗi>. Không sửa code production; nếu thấy bug thì báo, đừng sửa.
```

## Refactor không đổi hành vi
```text
Refactor <Class> để <mục tiêu: tách method dài, bỏ trùng lặp...>. Không đổi hành vi và API công khai.
Trước khi sửa, chạy test liên quan để có baseline; sau khi sửa chạy lại và so sánh.
```

## Review
```text
Dùng skill code-review cho diff hiện tại so với origin/main. Chỉ báo cáo, không sửa code.
```

## Ghi quyết định
```text
Dùng skill write-adr: chúng ta chọn <phương án> thay vì <phương án khác> vì <lý do>.
```

## Khi agent đi sai hướng
```text
Dừng lại. Tóm tắt: bạn đã thử gì, kết quả ra sao, giả thuyết hiện tại là gì.
Chưa sửa thêm cho đến khi tôi trả lời.
```

## Bàn giao sang phiên mới
```text
Viết bản tóm tắt bàn giao: mục tiêu, việc đã xong (file), việc còn lại, quyết định đã chốt,
lệnh để kiểm chứng. Ngắn gọn để dán vào phiên mới.
```
