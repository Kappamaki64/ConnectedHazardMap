package jp.ac.titech.itpro.sdl.connectedhazardmap.myData;

public class HazardMap {
    public int type;
    public String uri;
    public int usedPage;
    public float centerLat;
    public float centerLng;
    public float width;
    public float height;

    public HazardMap(
            int type,
            String uri,
            int usedPage,
            float centerLat,
            float centerLng,
            float width,
            float height
    ) {
        this.type = type;
        this.uri = uri;
        this.usedPage = usedPage;
        this.centerLat = centerLat;
        this.centerLng = centerLng;
        this.width = width;
        this.height = height;
    }
}
