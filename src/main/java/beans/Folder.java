package beans;

import java.util.ArrayList;
import java.util.List;

public class Folder {

    private int id;
    private String name;
    private int owner;
    private String creationDate;
    private List<SubFolder> subFolders;

    public Folder() {
        this.subFolders = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public List<SubFolder> getSubFolders() {
        return subFolders;
    }

    public void setSubFolders(List<SubFolder> subFolders) {
        this.subFolders = subFolders;
    }
}