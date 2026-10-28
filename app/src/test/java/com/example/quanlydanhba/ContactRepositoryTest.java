package com.example.quanlydanhba;

import static org.junit.Assert.*;

import com.example.quanlydanhba.data.ContactRepository;
import com.example.quanlydanhba.models.Contact;
import org.junit.Before;
import org.junit.Test;

public class ContactRepositoryTest {

    private ContactRepository repository;

    @Before
    public void setUp() {
        repository = ContactRepository.getInstance();
    }

    @Test
    public void testInitialSampleData() {
        assertNotNull(repository.getContacts());
        assertTrue(repository.getTotalCount() >= 3);
        assertTrue(repository.getFavoriteCount() >= 1);

        // Kiểm tra sự tồn tại của 3 liên hệ mẫu
        assertTrue(repository.isPhoneExists("0901234567", null));
        assertTrue(repository.isPhoneExists("0912345678", null));
        assertTrue(repository.isPhoneExists("0987654321", null));
    }

    @Test
    public void testInitialLetterExtraction() {
        Contact c1 = new Contact("Nguyễn Văn An", "0901234567", "Bạn bè", "Nam", false, "");
        assertEquals("A", c1.getInitialLetter());

        Contact c2 = new Contact("Trần Thị Bình", "0912345678", "Gia đình", "Nữ", true, "");
        assertEquals("B", c2.getInitialLetter());

        Contact c3 = new Contact("Lê Minh Châu", "0987654321", "Công việc", "Nam", false, "");
        assertEquals("C", c3.getInitialLetter());
    }

    @Test
    public void testAddAndDuplicatePhone() {
        int currentTotal = repository.getTotalCount();
        Contact newContact = new Contact("Phạm Hoàng Nam", "0933333333", "Bạn bè", "Nam", true, "Ghi chú test");
        repository.addContact(newContact);

        assertEquals(currentTotal + 1, repository.getTotalCount());
        assertTrue(repository.isPhoneExists("0933333333", null));

        // Kiểm tra loại trừ chính nó
        assertFalse(repository.isPhoneExists("0933333333", newContact.getId()));
    }

    @Test
    public void testUpdateFavorite() {
        Contact newContact = new Contact("Đỗ Thúy Vy", "0944444444", "Gia đình", "Nữ", false, "");
        repository.addContact(newContact);

        int favCountBefore = repository.getFavoriteCount();
        repository.updateFavorite(newContact.getId(), true);
        assertEquals(favCountBefore + 1, repository.getFavoriteCount());

        repository.updateFavorite(newContact.getId(), false);
        assertEquals(favCountBefore, repository.getFavoriteCount());
    }

    @Test
    public void testDeleteContact() {
        Contact toDelete = new Contact("Vũ Hải Đăng", "0955555555", "Công việc", "Nam", false, "");
        repository.addContact(toDelete);
        int totalBefore = repository.getTotalCount();

        boolean deleted = repository.deleteContact(toDelete.getId());
        assertTrue(deleted);
        assertEquals(totalBefore - 1, repository.getTotalCount());
        assertFalse(repository.isPhoneExists("0955555555", null));
    }
}
