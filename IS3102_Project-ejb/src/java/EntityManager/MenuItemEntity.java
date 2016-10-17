package EntityManager;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class MenuItemEntity extends ItemEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Lob
    private String imageURL;
    @OneToOne(cascade = {CascadeType.ALL}, mappedBy = "menuItem")
    private RecipeEntity recipe;
    
    @OneToMany(cascade={CascadeType.REMOVE}, mappedBy="menuItem")
    private List<SaleForecastEntity> saleForecastEntityList;    

    public MenuItemEntity() {
    }

    public MenuItemEntity(String SKU, String name, String category, String description, String imageURL, Integer _length, Integer width, Integer height) {
        super(SKU, _length, width, height);
        super.setName(name);
        super.setCategory(category);
        super.setDescription(description);
        this.imageURL = imageURL;
        super.setType("Menu Item");
        super.setIsDeleted(false);
    }

    public Boolean getIsDeleted() {
        return super.getIsDeleted();
    }

    public RecipeEntity getRecipe() {
        return recipe;
    }

    public void setRecipe(RecipeEntity recipe) {
        this.recipe = recipe;
    }

    public void setIsDeleted(Boolean isDeleted) {
        super.setIsDeleted(isDeleted);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MenuItemEntity)) {
            return false;
        }
        MenuItemEntity other = (MenuItemEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entityManagerBean.MenuItem[ id=" + id + " ]";
    }


}
