package com.example.android.itwasnews;

/**
 * This class contains information about the news
 */
public class News {
    private String section;
    private String title;
    private String publicationDate;
    private String url;

    public News(String section, String title, String publicationDate, String url) {
        this.section = section;
        this.title = title;
        this.publicationDate = publicationDate;
        this.url = url;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "News{" +
                "section='" + section + '\'' +
                ", title='" + title + '\'' +
                ", publicationDate='" + publicationDate + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
