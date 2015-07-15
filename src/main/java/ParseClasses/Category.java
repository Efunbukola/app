package ParseClasses;

/**
 * Created by Saboor Salaam on 7/3/2014.
 */
public class Category {
    String name;
    int size;

    public Category(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
