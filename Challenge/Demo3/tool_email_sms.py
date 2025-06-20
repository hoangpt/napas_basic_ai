import gspread
from google.oauth2.service_account import Credentials
import smtplib
from email.mime.text import MIMEText
from twilio.rest import Client

# --- Cấu hình Google Sheets ---
SCOPES = ['https://www.googleapis.com/auth/spreadsheets.readonly']
SERVICE_ACCOUNT_FILE = 'credentials.json'  # Đặt file này cùng thư mục script
SHEET_NAME = 'CustomerName'  # Đổi thành tên Google Sheet của bạn

# --- Cấu hình Email ---
EMAIL_ADDRESS = 'hoangphantich@gmail.com'  # Địa chỉ Gmail của bạn
EMAIL_PASSWORD = 'bdjc ttul jrqf utbv'    # App password của Gmail

# --- Cấu hình Twilio ---
TWILIO_SID = 'your_twilio_sid'
TWILIO_AUTH_TOKEN = 'your_twilio_auth_token'
TWILIO_PHONE = '+1234567890'  # Số điện thoại Twilio

# --- Đọc Google Sheet ---
creds = Credentials.from_service_account_file(SERVICE_ACCOUNT_FILE, scopes=SCOPES)
gc = gspread.authorize(creds)
sheet = gc.open(SHEET_NAME).sheet1  # Đổi tên sheet nếu không phải sheet1

rows = sheet.get_all_records()

# --- Gửi email và SMS ---
client = Client(TWILIO_SID, TWILIO_AUTH_TOKEN)

def send_email(name, email):
    subject = "Chào bạn, mời mua sản phẩm"
    body = f"Xin chào {name},\nChúng tôi xin giới thiệu sản phẩm mới..."
    msg = MIMEText(body)
    msg['Subject'] = subject
    msg['From'] = EMAIL_ADDRESS
    msg['To'] = email
    try:
        with smtplib.SMTP_SSL('smtp.gmail.com', 465) as smtp:
            smtp.login(EMAIL_ADDRESS, EMAIL_PASSWORD)
            smtp.send_message(msg)
        print(f"Đã gửi email cho {name} - {email}")
        return True
    except Exception as e:
        print(f"Lỗi gửi email cho {email}: {e}")
        return False

def send_sms(name, phone):
    sms_body = f"Chào {name}, bạn đã đăng ký thành công!"
    try:
        client.messages.create(
            body=sms_body,
            from_=TWILIO_PHONE,
            to=phone
        )
        print(f"Đã gửi SMS cho {name} - {phone}")
        return True
    except Exception as e:
        print(f"Lỗi gửi SMS cho {phone}: {e}")
        return False

def is_duplicate(email, phone, sent_emails, sent_phones):
    return email in sent_emails or phone in sent_phones

sent_emails = set()
sent_phones = set()

for row in rows:
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

print("Đã gửi email và SMS cho tất cả khách hàng.")
