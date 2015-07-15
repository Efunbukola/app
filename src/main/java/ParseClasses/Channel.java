package ParseClasses;

import java.util.List;

/**
 * Created by Saboor Salaam on 7/3/2014.
 */

public class Channel {
    public String channel_name, channel_id, cover_thumb, cover_title, description;
    public int size;
    public List<String> providers, categories;


    public Channel(String channel_name, String channel_id, String cover_thumb, String cover_title, String description, int size, List<String> providers, List<String> categories) {
        this.channel_name = channel_name;
        this.channel_id = channel_id;
        this.cover_thumb = cover_thumb;
        this.cover_title = cover_title;
        this.description = description;
        this.size = size;
        this.providers = providers;
        this.categories = categories;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getCover_thumb() {
        return cover_thumb;
    }

    public void setCover_thumb(String cover_thumb) {
        this.cover_thumb = cover_thumb;
    }

    public String getCover_title() {
        return cover_title;
    }

    public void setCover_title(String cover_title) {
        this.cover_title = cover_title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<String> getProviders() {
        return providers;
    }

    public void setProviders(List<String> providers) {
        this.providers = providers;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}