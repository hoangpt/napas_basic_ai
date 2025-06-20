# Tool giả lập gửi email và SMS từ một array khách hàng

def send_email(name, email):
    print(f"[EMAIL] Đã gửi email cho {name} - {email}")
    return True

def send_sms(name, phone):
    print(f"[SMS] Đã gửi SMS cho {name} - {phone}")
    return True

def is_duplicate(email, phone, sent_emails, sent_phones):
    return email in sent_emails or phone in sent_phones

# Danh sách khách hàng mẫu
customers = [
    {"Tên": "Nguyễn Văn A", "Email": "a@example.com", "Số điện thoại": "+84123456789"},
    {"Tên": "Trần Thị B", "Email": "b@example.com", "Số điện thoại": "+84987654321"},
    {"Tên": "Nguyễn Văn A", "Email": "a@example.com", "Số điện thoại": "+84123456789"},  # trùng
]

sent_emails = set()
sent_phones = set()

for row in customers:
    name = row.get('Tên') or row.get('Name')
    email = row.get('Email')
    phone = row.get('Số điện thoại') or row.get('Phone')

    if not email or not phone:
        print(f"Bỏ qua khách hàng thiếu thông tin: {name}")
        continue

    if is_duplicate(email, phone, sent_emails, sent_phones):
        print(f"Bỏ qua khách hàng trùng: {name} - {email} - {phone}")
        continue

    if send_email(name, email):
        sent_emails.add(email)
    if send_sms(name, phone):
        sent_phones.add(phone)

print("Đã giả lập gửi email và SMS cho tất cả khách hàng.")
