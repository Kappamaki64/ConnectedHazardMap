package jp.ac.titech.itpro.sdl.connectedhazardmap.myData;

public class HazardMap {
    public int type;
    public String uri;
    public int usedPageIndex;
    public float centerLat;
    public float centerLng;
    public float width;
    public float height;

    public HazardMap(
            int type,
            String uri,
            int usedPageIndex,
            float centerLat,
            float centerLng,
            float width,
            float height
    ) {
        this.type = type;
        this.uri = uri;
        this.usedPageIndex = usedPageIndex;
        this.centerLat = centerLat;
        this.centerLng = centerLng;
        this.width = width;
        this.height = height;
    }
}
