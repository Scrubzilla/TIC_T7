/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package displayclasses;

import javafx.scene.image.ImageView;

/**
 *
 * @author Nicklas
 */
public class Tutorial {
    private ImageView uploaderLogo;
    private String title;
    private String description;
    private String url;
    
    public Tutorial(ImageView uploaderLogo, String title, String description, String url) {
        this.uploaderLogo = uploaderLogo;
        this.title = title;
        this.description = description;
        this.url = url;
    }

    public ImageView getUploaderLogo() {
        return uploaderLogo;
    }

    public void setUploaderLogo(ImageView uploaderLogo) {
        this.uploaderLogo = uploaderLogo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getUrl(){
        return url;
    }
    
    public void setUrl(String url){
        this.url = url;
    }
}
