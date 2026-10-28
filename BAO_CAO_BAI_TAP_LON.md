# BÁO CÁO BÀI TẬP LỚN LẬP TRÌNH ANDROID
## ĐỀ TÀI: ỨNG DỤNG QUẢN LÝ DANH BẠ LIÊN HỆ

---

### THÔNG TIN SINH VIÊN
* **Họ và tên:** *[Điền Họ và tên của bạn vào đây]*
* **Mã sinh viên (MSSV):** *[Điền Mã sinh viên vào đây]*
* **Lớp:** *[Điền Tên lớp học phần vào đây]*
* **Link Git Repository:** *[Điền Link GitHub/GitLab của bạn vào đây, ví dụ: https://github.com/username/MSSV_HoTen_QuanLyDanhBa]*

---

## I. TỔNG QUAN DỰ ÁN
* **Nền tảng & Ngôn ngữ:** Android SDK 34 (Android 14) / Min SDK 24, ngôn ngữ Java, giao diện XML Layout.
* **Phong cách UI:** **Material You 3 Expressive** (Thiết kế thẻ bo tròn mềm mại 16dp, màu sắc pastel hài hòa, dynamic letter avatar, hiệu ứng tương tác trực quan).
* **Kiến trúc quản lý dữ liệu:** Lưu trữ trong bộ nhớ in-memory bằng `ContactRepository` quản lý danh sách `ArrayList<Contact>`, kết hợp điều hướng dữ liệu qua lại giữa 4 Activity bằng Explicit Intent và Implicit Intent (`ACTION_DIAL`).

---

## II. NỘI DUNG CHI TIẾT 4 MÀN HÌNH

### 1. Màn hình 1: Đăng nhập (`LoginActivity`)

#### 1.1. Hình ảnh chụp màn hình thực tế

| Trường hợp bình thường | Trường hợp báo lỗi (bỏ trống hoặc sai mật khẩu) |
| :---: | :---: |
| *(Dán ảnh chụp màn hình đăng nhập lúc ban đầu vào đây)* | *(Dán ảnh chụp màn hình khi bấm đăng nhập báo lỗi vào đây)* |
| *Hình 1.1. Giao diện Đăng nhập* | *Hình 1.2. Thông báo lỗi khi nhập sai hoặc để trống* |

---

#### 1.2. Mô tả chi tiết kỹ thuật đã thực hiện
* **Các thành phần giao diện đã thêm (`activity_login.xml`):**
  * `MaterialCardView` (`cardLogo`): Khối bo tròn chứa icon danh bạ thương hiệu.
  * `TextInputLayout` & `TextInputEditText` (`tilUsername`, `etUsername`): Nhập tên đăng nhập, có icon người dùng ở đầu.
  * `TextInputLayout` & `TextInputEditText` (`tilPassword`, `etPassword`): Nhập mật khẩu với icon ổ khóa, thuộc tính `inputType="textPassword"` và tính năng bật/tắt ẩn hiện ký tự (`passwordToggleEnabled="true"`).
  * `TextView` (`tvErrorMessage`): Hiển thị thông báo lỗi màu đỏ khi thông tin không hợp lệ.
  * `MaterialButton` (`btnLogin`): Nút "Đăng nhập" bo cong 12dp.
* **Khai báo tài khoản cố định:** Được khai báo dưới dạng hằng số `private static final` ngay trong lớp `LoginActivity.java`:
  ```java
  private static final String FIXED_USERNAME = "admin";
  private static final String FIXED_PASSWORD = "123456";
  ```
* **Cách kiểm tra bỏ trống & sai thông tin:**
  * Hàm `handleLogin()` đọc chuỗi nhập vào từ EditText và xóa khoảng trắng thừa (`trim()`).
  * Sử dụng `TextUtils.isEmpty()` kiểm tra nếu tên đăng nhập hoặc mật khẩu rỗng thì gọi hàm `setError()` trên `TextInputLayout` và hiển thị cảnh báo *"Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu"*.
  * Nếu người dùng nhập sai tên hoặc mật khẩu so với hằng số `admin` / `123456` thì hiển thị thông báo lỗi *"Sai tên đăng nhập hoặc mật khẩu"* và giữ nguyên màn hình đăng nhập.
* **Cách chuyển sang `MainActivity` bằng Intent:**
  * Khi thông tin hợp lệ, tạo Explicit Intent mở `MainActivity` và gọi `finish()` để giải phóng `LoginActivity` khỏi ngăn xếp (back stack). Nhờ đó, người dùng bấm phím Back từ `MainActivity` sẽ thoát ứng dụng chứ không quay lại màn hình đăng nhập:
  ```java
  if (FIXED_USERNAME.equals(username) && FIXED_PASSWORD.equals(password)) {
      Intent intent = new Intent(LoginActivity.this, MainActivity.class);
      startActivity(intent);
      finish(); // Đóng LoginActivity để không thể Back lại
  }
  ```

---

### 2. Màn hình 2: Danh sách liên hệ (`MainActivity`)

#### 2.1. Hình ảnh chụp màn hình thực tế

| Danh sách ban đầu kèm thống kê | Sau khi tương tác đổi Yêu thích |
| :---: | :---: |
| *(Dán ảnh chụp danh sách có 3 liên hệ mẫu & 2 thẻ thống kê vào đây)* | *(Dán ảnh chụp khi bấm dấu sao yêu thích, thống kê tự đổi số)* |
| *Hình 2.1. Danh sách liên hệ và khối thống kê ban đầu* | *Hình 2.2. Trạng thái cập nhật sau khi đổi dấu sao yêu thích* |

---

#### 2.2. Mô tả chi tiết kỹ thuật đã thực hiện
* **Các thành phần giao diện đã thêm (`activity_main.xml` & `item_contact.xml`):**
  * **Khối thống kê 2 thẻ (`layoutStats`):**
    * Thẻ 1 (`cardTotalContacts`): Hiển thị tiêu đề "Tổng liên hệ" và `TextView` (`tvTotalCount`) hiển thị số lượng tổng.
    * Thẻ 2 (`cardFavoriteContacts`): Hiển thị tiêu đề "Yêu thích" và `TextView` (`tvFavoriteCount`) hiển thị số lượng liên hệ yêu thích.
  * **Danh sách (`rvContacts`):** Sử dụng `RecyclerView` kết hợp `LinearLayoutManager` cho hiệu năng mượt mà.
  * **Item liên hệ (`item_contact.xml`):** Mỗi dòng hiển thị:
    * `layoutAvatar`: Khung tròn chứa chữ cái đầu tiên của tên (`tvAvatarInitial`). Màu nền avatar tự động biến thiên dựa trên mã băm hash của tên liên hệ qua tiện ích `AvatarColorHelper`.
    * `tvContactName` & `tvContactPhone`: Họ tên và số điện thoại.
    * `tvGroupBadge`: Chip badge hiển thị tên nhóm (Gia đình, Bạn bè, Công việc, Khác) có màu nền và màu chữ riêng biệt.
    * `ivFavorite`: ImageView hiển thị icon ngôi sao (vàng khi yêu thích, xám khi chưa yêu thích).
  * `fabAddContact`: Nút tròn nổi `ExtendedFloatingActionButton` góc dưới phải để mở màn hình thêm mới.
* **Dữ liệu mẫu ban đầu:**
  Khởi tạo sẵn 3 liên hệ trong `ContactRepository.java`:
  1. Phạm Quốc Tuấn | `0982123456` | Bạn bè | Nam | Không yêu thích | Ghi chú: "Bạn thân thời cấp 3."
  2. Hoàng Thùy Dương | `0915789654` | Gia đình | Nữ | Có yêu thích | Ghi chú: "Chị gái ruột."
  3. Đỗ Minh Khang | `0973456789` | Công việc | Nam | Không yêu thích | Ghi chú: "Đồng nghiệp cùng phòng ban."
* **Cách tính và đồng bộ thống kê:**
  * Lớp `ContactRepository` cung cấp 2 hàm: `getTotalCount()` (lấy `size()`) và `getFavoriteCount()` (duyệt đếm các phần tử có `isFavorite() == true`).
  * Trong `MainActivity.java`, ghi đè hàm vòng đời `onResume()` để gọi `refreshData()`, đảm bảo mỗi khi quay lại từ màn hình Thêm hoặc Chi tiết thì số liệu và danh sách luôn được đồng bộ mới nhất:
  ```java
  @Override
  protected void onResume() {
      super.onResume();
      refreshData();
  }

  private void refreshData() {
      adapter.notifyDataSetChanged();
      tvTotalCount.setText(String.valueOf(repository.getTotalCount()));
      tvFavoriteCount.setText(String.valueOf(repository.getFavoriteCount()));
  }
  ```
* **Xử lý sự kiện:**
  * **Nhấn vào dấu sao yêu thích:** Chuyển đổi trạng thái `isFavorite = !isFavorite`, cập nhật vào repository và gọi `adapter.notifyItemChanged(position)`, cập nhật trực tiếp `tvFavoriteCount`.
  * **Nhấn vào một dòng liên hệ:** Kích hoạt Explicit Intent mở `ContactDetailActivity`, đính kèm ID của liên hệ qua `intent.putExtra(ContactDetailActivity.EXTRA_CONTACT_ID, contact.getId())`.
  * **Nhấn nút Thêm:** Kích hoạt Intent mở `AddContactActivity`.

---

### 3. Màn hình 3: Thêm liên hệ (`AddContactActivity`)

#### 3.1. Hình ảnh chụp màn hình thực tế

| Nhập liệu báo lỗi (bỏ trống/sai SĐT/trùng SĐT) | Nhập liệu đầy đủ & Lưu thành công |
| :---: | :---: |
| *(Dán ảnh chụp khi nhập sai định dạng hoặc trùng số vào đây)* | *(Dán ảnh chụp form nhập hợp lệ và thông báo Toast thành công)* |
| *Hình 3.1. Kiểm tra validation và báo lỗi* | *Hình 3.2. Nhập thông tin hợp lệ và lưu liên hệ* |

---

#### 3.2. Mô tả chi tiết kỹ thuật đã thực hiện
* **Các thành phần giao diện đã thêm (`activity_add_contact.xml`):**
  * `btnBack`: Nút mũi tên quay lại trên thanh tiêu đề.
  * `tilName` & `etName`: `TextInputLayout` nhập Họ và tên (bắt buộc).
  * `tilPhone` & `etPhone`: `TextInputLayout` nhập Số điện thoại với `inputType="phone"` (bắt buộc).
  * `spGroup`: `Spinner` chọn nhóm liên hệ, nạp dữ liệu từ mảng `R.array.contact_groups` gồm: *Gia đình, Bạn bè, Công việc, Khác*.
  * `rgGender`: `RadioGroup` chứa 3 `MaterialRadioButton` (*Nam, Nữ, Khác* - mặc định chọn Nam).
  * `cbFavorite`: `MaterialCheckBox` đánh dấu "Đánh dấu là liên hệ yêu thích".
  * `tilNotes` & `etNotes`: `TextInputLayout` nhập ghi chú đa dòng (`inputType="textMultiLine"`, `lines="3"`).
  * `btnSave`: Nút MaterialButton "Lưu liên hệ".
* **Cách kiểm tra dữ liệu hợp lệ & không trùng số:**
  * **Kiểm tra rỗng tên:** `TextUtils.isEmpty(name)` -> nếu rỗng thì `tilName.setError("Họ và tên không được để trống")`.
  * **Kiểm tra định dạng SĐT (10 số, bắt đầu bằng 0):** Sử dụng biểu thức chính quy Regex:
    ```java
    if (TextUtils.isEmpty(phone) || !phone.matches("^0[0-9]{9}$")) {
        tilPhone.setError("Số điện thoại phải gồm 10 chữ số và bắt đầu bằng số 0");
        return;
    }
    ```
  * **Kiểm tra trùng số điện thoại:** Gọi hàm `repository.isPhoneExists(phone, null)`. Hàm này duyệt qua toàn bộ danh sách `contactList`, so sánh số điện thoại chuẩn hóa; nếu đã tồn tại thì báo lỗi:
    ```java
    if (repository.isPhoneExists(phone, null)) {
        tilPhone.setError("Số điện thoại này đã tồn tại trong danh bạ");
        return;
    }
    ```
* **Tạo đối tượng `Contact` và trả về danh sách:**
  * Khi các điều kiện hợp lệ, khởi tạo đối tượng `Contact` mới và thêm vào đầu danh sách thông qua `repository.addContact(newContact)`:
  ```java
  Contact newContact = new Contact(name, phone, group, gender, isFavorite, notes);
  repository.addContact(newContact);
  Toast.makeText(this, "Đã thêm liên hệ thành công", Toast.LENGTH_SHORT).show();
  finish(); // Đóng màn hình, trở về MainActivity
  ```

---

### 4. Màn hình 4: Chi tiết liên hệ (`ContactDetailActivity`)

#### 4.1. Hình ảnh chụp màn hình thực tế

| Giao diện Chi tiết liên hệ | Màn hình cuộc gọi (ACTION_DIAL) | Hộp thoại xác nhận Xóa liên hệ |
| :---: | :---: | :---: |
| *(Dán ảnh chi tiết thông tin, avatar)* | *(Dán ảnh màn hình bàn phím quay số sau khi bấm Gọi)* | *(Dán ảnh hộp thoại AlertDialog xác nhận xóa)* |
| *Hình 4.1. Chi tiết thông tin liên hệ* | *Hình 4.2. Mở ứng dụng gọi điện hệ thống* | *Hình 4.3. Xác nhận xóa liên hệ* |

---

#### 4.2. Mô tả chi tiết kỹ thuật đã thực hiện
* **Các thành phần giao diện đã thêm (`activity_contact_detail.xml`):**
  * `btnBackTop` & `btnBackBottom`: Nút quay lại ở thanh tiêu đề và ở chân trang.
  * **Hero Profile Banner:** `layoutAvatarDetail` chứa avatar tròn cỡ lớn 84dp hiển thị chữ cái đầu của tên (`tvAvatarDetailInitial`), Họ và tên (`tvDetailName`) và Badge nhóm (`tvDetailGroupBadge`).
  * `tvDetailPhone`: `TextView` hiển thị số điện thoại.
  * `tvDetailGroupAndGender`: `TextView` hiển thị kết hợp nhóm và giới tính liên hệ.
  * `tvDetailNotes`: `TextView` hiển thị nội dung ghi chú (hoặc *"Không có ghi chú nào"* nếu để trống).
  * `cbDetailFavorite`: `MaterialCheckBox` trạng thái yêu thích, cho phép người dùng tick/bỏ tick để cập nhật trực tiếp.
  * `btnCall`: Nút "Gọi điện" màu xanh lục emerald (`#10B981`) có icon cuộc gọi.
  * `btnDelete`: Nút "Xóa liên hệ" màu cảnh báo mềm (`#DC2626`) có icon thùng rác.
* **Cách nhận dữ liệu qua Intent:**
  * Nhận ID liên hệ từ Intent: `contactId = getIntent().getStringExtra(EXTRA_CONTACT_ID);`.
  * Truy vấn đối tượng `Contact` từ `ContactRepository.getInstance().getContactById(contactId)` và hiển thị dữ liệu lên các View tương ứng.
* **Xử lý gọi điện bằng Implicit Intent (`ACTION_DIAL`):**
  * Sử dụng Intent ngầm định với hành động `Intent.ACTION_DIAL` và dữ liệu `Uri.parse("tel:" + phone)`. Hành động này mở ứng dụng gọi điện của hệ thống và điền sẵn số điện thoại mà không cần xin quyền nguy hiểm `CALL_PHONE`:
  ```java
  Intent dialIntent = new Intent(Intent.ACTION_DIAL);
  dialIntent.setData(Uri.parse("tel:" + currentContact.getPhone()));
  startActivity(dialIntent);
  ```
* **Xử lý đổi Yêu thích, nút Quay lại và Xóa liên hệ:**
  * **Đổi yêu thích:** Bắt sự kiện `cbDetailFavorite.setOnCheckedChangeListener`:
    ```java
    currentContact.setFavorite(isChecked);
    repository.updateFavorite(currentContact.getId(), isChecked);
    ```
  * **Nút Quay lại:** Gọi hàm `finish()` để đóng màn hình chi tiết, `MainActivity` ở chế độ `onResume()` sẽ tự động cập nhật lại danh sách và thống kê.
  * **Nút Xóa liên hệ:** Để tăng trải nghiệm người dùng và tránh xóa nhầm, một `AlertDialog` xác nhận được hiển thị. Khi người dùng xác nhận "Xóa", hàm `repository.deleteContact(id)` được gọi và đóng màn hình:
    ```java
    new AlertDialog.Builder(this)
        .setTitle("Xác nhận xóa")
        .setMessage("Bạn có chắc chắn muốn xóa liên hệ này khỏi danh bạ?")
        .setPositiveButton("Xóa", (dialog, which) -> {
            repository.deleteContact(currentContact.getId());
            Toast.makeText(this, "Đã xóa liên hệ", Toast.LENGTH_SHORT).show();
            finish();
        })
        .setNegativeButton("Hủy", null)
        .show();
    ```

---

## III. TỔNG KẾT & ĐÁNH GIÁ MỨC ĐỘ HOÀN THÀNH

| STT | Yêu cầu trong đề bài | Tình trạng | Ghi chú |
| :---: | :--- | :---: | :--- |
| 1 | Màn hình Đăng nhập (tài khoản `admin`/`123456`, báo lỗi rỗng/sai, không back lại được) | **100% Hoàn thành** | Đã hoàn thiện bằng `LoginActivity` và kiểm tra hợp lệ chi tiết. |
| 2 | Màn hình Danh sách (hiển thị đủ thông tin, avatar chữ cái đầu, badge nhóm, sao yêu thích) | **100% Hoàn thành** | Đã triển khai bằng `RecyclerView` thẻ bo cong hiện đại. |
| 3 | Khối thống kê (Tổng liên hệ & Yêu thích tự cập nhật) | **100% Hoàn thành** | Tự động tính toán và cập nhật theo thời gian thực. |
| 4 | Màn hình Thêm liên hệ (đủ thành phần, regex 10 số bắt đầu bằng 0, chống trùng SĐT) | **100% Hoàn thành** | Đầy đủ validation báo lỗi rõ ràng. |
| 5 | Màn hình Chi tiết (đủ thông tin, nút gọi điện qua `ACTION_DIAL`, đổi yêu thích, xóa liên hệ) | **100% Hoàn thành** | Có hộp thoại xác nhận xóa thân thiện. |
| 6 | Cấu hình Git (.gitignore loại trừ file rác, mã nguồn đẩy lên Git) | **100% Hoàn thành** | Đã khởi tạo Git và commit đầy đủ mã nguồn. |
