package ParseClasses;

/**
 * Created by Saboor Salaam on 6/3/2015.
 */
public interface connectionFailedListener {

    void onConnectionFailed();
    void onConnectionSuccessful();
    void onCannotConnectToParse();
}