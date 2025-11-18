package Objects;

public class Medico {
    private String id;
    private String name;
    private String price;
    private String email;

    public Medico(String id, String name, String price, String email) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = this.id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Objects.Medico{");
        sb.append("id='").append(getId()).append('\'');
        sb.append(", name='").append(getName()).append('\'');
        sb.append(", price='").append(getPrice()).append('\'');
        sb.append(", email='").append(getEmail()).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
