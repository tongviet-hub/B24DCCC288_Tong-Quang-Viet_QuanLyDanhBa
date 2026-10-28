# Ứng dụng Quản lý Danh bạ (Contact Manager)

Ứng dụng Android viết bằng **Java** và **XML Layout**, tuân thủ phong cách thiết kế **Material You 3 Expressive (Clean Soft Cards & Vibrant Pastel Accents)**.

---

## 📱 Tính năng & 4 Màn hình chính

### 1. Màn hình 1: Đăng nhập (`LoginActivity`)
- **Tài khoản cố định:** `admin` / `123456`.
- Báo lỗi cụ thể trên `TextInputLayout` khi để trống hoặc nhập sai thông tin.
- Đăng nhập thành công: Dùng `Intent` chuyển sang `MainActivity` và gọi `finish()` (nút Back sẽ không quay lại màn hình đăng nhập).

### 2. Màn hình 2: Danh sách liên hệ (`MainActivity`)
- **Khối thống kê song song (Dual Stats Cards):** Hiển thị tổng số liên hệ và số liên hệ yêu thích, cập nhật thời gian thực khi thêm, xóa hoặc toggle yêu thích.
- **Danh sách liên hệ (`RecyclerView`):**
  - Avatar tròn chữ cái đầu với màu sắc pastel biến thiên tự động.
  - Họ tên đậm nét, số điện thoại định dạng chuẩn.
  - Tag chip nhóm liên hệ (*Gia đình, Bạn bè, Công việc, Khác*) được mã hóa màu riêng biệt.
  - Icon ngôi sao: chạm trực tiếp để bật/tắt trạng thái yêu thích tức thì.
- **3 liên hệ mẫu ban đầu:**
  1. Nguyễn Văn An | `0901234567` | Bạn bè | Nam | Không yêu thích
  2. Trần Thị Bình | `0912345678` | Gia đình | Nữ | Yêu thích
  3. Lê Minh Châu | `0987654321` | Công việc | Nam | Không yêu thích
- Nhấn vào một liên hệ để chuyển sang màn hình Chi tiết.
- Nút FAB "Thêm liên hệ" để mở màn hình thêm mới.

### 3. Màn hình 3: Thêm liên hệ (`AddContactActivity`)
- **Họ và tên:** `EditText`, bắt buộc nhập.
- **Số điện thoại:** `EditText` (inputType="phone"), kiểm tra biểu thức chính quy `^0[0-9]{9}$` (đủ 10 số, bắt đầu bằng số 0) và kiểm tra trùng số với danh bạ hiện có.
- **Nhóm liên hệ:** `Spinner` với 4 lựa chọn (*Gia đình, Bạn bè, Công việc, Khác*).
- **Giới tính:** `RadioGroup` gồm 3 lựa chọn (*Nam, Nữ, Khác* - mặc định là Nam).
- **Yêu thích:** `CheckBox` đánh dấu yêu thích.
- **Ghi chú:** `EditText` đa dòng (`textMultiLine`).
- **Nút Lưu liên hệ:** Tạo đối tượng `Contact`, lưu vào `ContactRepository`, hiển thị thông báo thành công và trở về danh sách.

### 4. Màn hình 4: Chi tiết liên hệ (`ContactDetailActivity`)
- Hiển thị đầy đủ thông tin: Avatar tròn lớn, họ tên, số điện thoại, phân loại nhóm & giới tính, nội dung ghi chú.
- **CheckBox Yêu thích:** Có thể đổi trực tiếp trạng thái yêu thích của liên hệ.
- **Nút Gọi điện:** Sử dụng Intent ngầm định (`ACTION_DIAL`) truyền `tel:<số_điện_thoại>` để mở ứng dụng gọi điện của hệ thống.
- **Nút Quay lại:** Lưu thay đổi và đóng màn hình (`finish()`).
- **Nút Xóa liên hệ:** Hiển thị `AlertDialog` xác nhận trước khi xóa; xóa khỏi danh bạ và tự động cập nhật lại thống kê trên màn hình chính.

---

## 🛠️ Công nghệ & Thư viện sử dụng
- **Ngôn ngữ:** Java (JDK 17 / Java 17).
- **Nền tảng:** Android SDK 34 (Android 14) / Min SDK 24 (Android 7.0).
- **UI Framework:** Material Design 3 (`com.google.android.material:material:1.11.0`), AndroidX AppCompat, RecyclerView, CardView.
- **Lưu trữ dữ liệu:** Singleton `ContactRepository` quản lý `ArrayList<Contact>` in-memory.

---

## 🚀 Hướng dẫn mở dự án trong Android Studio
1. Mở **Android Studio** -> Chọn **File** > **Open...**
2. Điều hướng đến thư mục `TH2` và nhấn **OK**.
3. Chờ Android Studio đồng bộ Gradle (Sync Project with Gradle Files).
4. Chọn thiết bị ảo (Emulator) hoặc điện thoại thật rồi nhấn nút **Run 'app'** (Shift + F10).
