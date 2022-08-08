package jp.ac.titech.itpro.sdl.connectedhazardmap.myData;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Data {
    public static final Map<String, Place> placeMap;

    static {
        placeMap = new LinkedHashMap<>();

        Place place;
        List<HazardMap> hazardMaps;

        hazardMaps = new ArrayList<>();
        hazardMaps.add(new HazardMap(1, "https://www.city.chiyoda.lg.jp/documents/2086/shiryo8-9_2.pdf", 0, 35.69114f, 139.78728f, 8000, 5653.762f));
        place = new Place("Chiyoda", "千代田区", 35.69411351250211f, 139.75330682513686f, hazardMaps);
        placeMap.put(place.placeName, place);

        hazardMaps = new ArrayList<>();
        hazardMaps.add(new HazardMap(1, "https://www.city.chuo.lg.jp/bosai/bosai/suigaisonae/kouzuihazard/kozui02.files/R4arakawatizu.pdf", 0, 35.671673f, 139.77394f, 4700, 6647.1436f));
        place = new Place("Chuo", "中央区", 35.670409667993006f, 139.77201791529674f, hazardMaps);
        placeMap.put(place.placeName, place);

        hazardMaps = new ArrayList<>();
        place = new Place("Minato", "港区", 35.657850024752165f, 139.7515677320577f, hazardMaps);
        placeMap.put(place.placeName, place);

        hazardMaps = new ArrayList<>();
        place = new Place("Shinjuku", "新宿区", 35.69398170907101f, 139.70338504290092f, hazardMaps);
        placeMap.put(place.placeName, place);

        hazardMaps = new ArrayList<>();
        place = new Place("Bunkyo", "文京区", 35.708117276349675f, 139.75213010746486f, hazardMaps);
        placeMap.put(place.placeName, place);

        hazardMaps = new ArrayList<>();
        hazardMaps.add(new HazardMap(1, "https://www.city.taito.lg.jp/bosai/map/saigai/hazardmap.files/arakawa_omote.pdf", 0, 35.713528f, 139.78589f, 4400, 6222.858f));
        place = new Place("Taito", "台東区", 35.712560182704074f, 139.78002508126815f, hazardMaps);
        placeMap.put(place.placeName, place);

        hazardMaps = new ArrayList<>();
        hazardMaps.add(new HazardMap(1, "https://www.city.sumida.lg.jp/anzen_anshin/bousai/suigai/suigai.files/kouzui_hukasa.pdf", 0, 35.71f, 139.81505f, 6100, 8541.05f));
        place = new Place("Sumida", "墨田区", 35.710743880187366f, 139.80155869793938f, hazardMaps);
        placeMap.put(place.placeName, place);

        hazardMaps = new ArrayList<>();
        hazardMaps.add(new HazardMap(1, "https://www.city.koto.lg.jp/470601/documents/kouzuimap.pdf", 0, 35.6449f, 139.80554f, 10750, 15192.993f));
        place = new Place("Koto", "江東区", 35.67285711879735f, 139.8173300869595f, hazardMaps);
        placeMap.put(place.placeName, place);

        hazardMaps = new ArrayList<>();
        hazardMaps.add(new HazardMap(2, "https://www.city.shinagawa.tokyo.jp/ct/pdf/hpg000020939_2.pdf", 0, 35.59859f, 139.7371f, 2600, 3674.0552f));
        place = new Place("Shinagawa", "品川区", 35.60902119366901f, 139.7303381950257f, hazardMaps);
        placeMap.put(place.placeName, place);

        hazardMaps = new ArrayList<>();
        place = new Place("Meguro", "目黒区", 35.6413617236609f, 139.69815511018555f, hazardMaps);
        placeMap.put(place.placeName, place);

        hazardMaps = new ArrayList<>();
        hazardMaps.add(new HazardMap(2, "http://www.city.ota.tokyo.jp/seikatsu/chiiki/bousai/suigai/hazardmap.files/1-1tamagawa.pdf", 0, 35.57414f, 139.72173f, 14750, 10436.555f));
        place = new Place("Ota", "大田区", 35.56125418300611f, 139.71598835356352f, hazardMaps);
        placeMap.put(place.placeName, place);

        hazardMaps = new ArrayList<>();
        hazardMaps.add(new HazardMap(2, "https://www.city.setagaya.lg.jp/mokuji/kurashi/005/003/003/d00005601_d/fil/3.pdf", 0, 35.61759f, 139.59526f, 17750, 10038.056f));
        place = new Place("Setagaya", "世田谷区", 35.6465854729032f, 139.65331596525746f, hazardMaps);
        placeMap.put(place.placeName, place);

        hazardMaps = new ArrayList<>();
        place = new Place("Shibuya", "渋谷区", 35.663692916670655f, 139.6977409001403f, hazardMaps);
        placeMap.put(place.placeName, place);

        hazardMaps = new ArrayList<>();
        place = new Place("Nakano", "中野区", 35.70734829209656f, 139.66377276910302f, hazardMaps);
        placeMap.put(place.placeName, place);

        hazardMaps = new ArrayList<>();
        place = new Place("Suginami", "杉並区", 35.699498476540846f, 139.6364035083031f, hazardMaps);
        placeMap.put(place.placeName, place);

        hazardMaps = new ArrayList<>();
        place = new Place("Toshima", "豊島区", 35.72613736037975f, 139.71668279890645f, hazardMaps);
        placeMap.put(place.placeName, place);

        hazardMaps = new ArrayList<>();
        place = new Place("Kita", "北区", 35.752918306047604f, 139.73393709702495f, hazardMaps);
        placeMap.put(place.placeName, place);

        hazardMaps = new ArrayList<>();
        hazardMaps.add(new HazardMap(1, "https://www.city.arakawa.tokyo.jp/documents/576/0412_arakawakuhm_map.pdf", 0, 35.745064f, 139.7872f, 7250, 5130.8774f));
        place = new Place("Arakawa", "荒川区", 35.735933199343776f, 139.7831871158724f, hazardMaps);
        placeMap.put(place.placeName, place);

        hazardMaps = new ArrayList<>();
        hazardMaps.add(new HazardMap(1, "https://www.city.itabashi.tokyo.jp/_res/projects/default_project/_page_/001/005/633/arakawahanranban.pdf", 0, 35.767635f, 139.6739f, 12000, 8484.849f));
        place = new Place("Itabashi", "板橋区", 35.751132942147f, 139.70949375353518f, hazardMaps);
        placeMap.put(place.placeName, place);

        hazardMaps = new ArrayList<>();
        place = new Place("Nerima", "練馬区", 35.73572992479742f, 139.65177811692217f, hazardMaps);
        placeMap.put(place.placeName, place);

        hazardMaps = new ArrayList<>();
        hazardMaps.add(new HazardMap(1, "https://www.city.adachi.tokyo.jp/documents/2808/hm05-08.pdf", 0, 35.77842f, 139.80167f, 13000, 9198.32f));
        place = new Place("Adachi", "足立区", 35.77497706568685f, 139.8044180254643f, hazardMaps);
        placeMap.put(place.placeName, place);

        hazardMaps = new ArrayList<>();
        hazardMaps.add(new HazardMap(1, "https://www.city.katsushika.lg.jp/_res/projects/default_project/_page_/001/022/522/r4arakawanakagawaedogawahazard.pdf", 0, 35.754486f, 139.85179f, 7600, 10748.572f));
        place = new Place("Katsushika", "葛飾区", 35.74348525905646f, 139.84719871020306f, hazardMaps);
        placeMap.put(place.placeName, place);

        hazardMaps = new ArrayList<>();
        hazardMaps.add(new HazardMap(1, "https://www.city.edogawa.tokyo.jp/documents/519/step5-3.pdf", 0, 35.69279f, 139.87497f, 10750, 15194.541f));
        place = new Place("Edogawa", "江戸川区", 35.70665303810955f, 139.8684263365995f, hazardMaps);
        placeMap.put(place.placeName, place);
    }
}
