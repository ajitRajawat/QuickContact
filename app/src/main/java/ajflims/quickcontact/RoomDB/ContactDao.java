package ajflims.quickcontact.RoomDB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by ajit on 9/14/2018.
 */

@Dao
public interface ContactDao {

    @Insert
    public void addContact(Contact contact);

    @Query("select * from Contact")
    public List<Contact> getContact();

    @Update
    public void updateContact(Contact contact);

    @Delete
    public void deleteContact(Contact contact);

    @Delete
    public void deleteAll(List<Contact> contacts);

}

