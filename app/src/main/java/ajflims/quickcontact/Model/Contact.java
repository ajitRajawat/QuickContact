package ajflims.quickcontact.Model;

/**
 * Created by ajit on 10/13/2018.
 */

public class Contact {

    private String name,number,id,key,image;

    public Contact(String name, String number, String id, String key, String image) {
        this.name = name;
        this.number = number;
        this.id = id;
        this.key = key;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
