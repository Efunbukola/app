package ParseClasses;

/**
 * Created by Saboor Salaam on 6/6/2014.
 */
public class Video {
    String provider_thumbnail;
    String provider_name;
    String provider_id;
    String channel_name;

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    String channel_id;
    String videoId;
    String datePublished;
    String videoTitle;
    String videoDescription;
    String videoThumbnail;
    String type;

    public Video(String provider_thumbnail, String provider_name, String provider_id, String channel_name, String channel_id, String videoId, String datePublished, String videoTitle, String videoDescription, String videoThumbnail, String type) {
        this.provider_thumbnail = provider_thumbnail;
        this.provider_name = provider_name;
        this.provider_id = provider_id;
        this.channel_name = channel_name;
        this.videoId = videoId;
        this.datePublished = datePublished;
        this.videoTitle = videoTitle;
        this.videoDescription = videoDescription;
        this.videoThumbnail = videoThumbnail;
        this.type = type;
        this.channel_id = channel_id;
    }

    public String getProvider_thumbnail() {
        return provider_thumbnail;
    }

    public void setProvider_thumbnail(String provider_thumbnail) {
        this.provider_thumbnail = provider_thumbnail;
    }

    public String getProvider_name() {
        return provider_name;
    }

    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    public void setVideoDescription(String videoDescription) {
        this.videoDescription = videoDescription;
    }

    public String getVideoThumbnail() {
        return videoThumbnail;
    }

    public void setVideoThumbnail(String videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
