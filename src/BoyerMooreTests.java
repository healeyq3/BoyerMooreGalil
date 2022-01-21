import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;

/**
 * These tests are meant to ensure three specifications:
 * 1. The algorithms function properly.
 * 2. The good suffix heuristic was implemented properly.
 * 3. The Galil Rule was implemented properly.
 *
 * Two important notes about these tests:
 * 1. The majority (if not all) of the tests for specification one have been appropriated from the GaTech CS 1332 TAs,
 * as well as Jack Smalligan and Ruston Shome.
 * 2. Specification three is the only place where we are looking for a different results between the two BM
 * implementations.
 *
 * @author Quill Healey
 * @version 1.0
 */
public class BoyerMooreTests {

    private static final int TIMEOUT = 200;

    private String kmpPattern;
    private int[] expFailureTable;
    private String sellPattern;
    private String sellText;
    private String sellNoMatch;
    private List<Integer> sellAnswer;

    private String multiplePattern;
    private String multipleText;
    private List<Integer> multipleAnswer;

    private List<Integer> emptyList;

    private Map<Character, Integer> expLastOccTable;
    private List<Integer> expMatches;

    private CharSequence pattern;
    private CharSequence text;

    private CharacterComparator comparator;

    @Before
    public void setUp() {
        kmpPattern = "ababa";
        sellPattern = "sell";
        sellText = "She sells seashells by the seashore.";
        sellNoMatch = "sea lions trains cardinal boardwalk";

        sellAnswer = new ArrayList<>();
        sellAnswer.add(4);

        multiplePattern = "ab";
        multipleText = "abab";

        multipleAnswer = new ArrayList<>();
        multipleAnswer.add(0);
        multipleAnswer.add(2);

        emptyList = new ArrayList<>();

        expLastOccTable = new HashMap<>();
        expMatches = new ArrayList<>();

        comparator = new CharacterComparator();
    }

    /**************************************************************************************
     SPECIFICATION ONE.
     ***********************************************************************************/

    /* ------------------------------ Last Occurrence Table ---------------------------- */

    /**
     * A rather generic test, not checking any edge case.
     * TA test.
     */
    @Test(timeout = TIMEOUT)
    public void testBuildLastTable() {
        /*
            pattern: sell
            last table: {s : 0, e : 1, l : 3}
         */
        Map<Character, Integer> lastTable = BoyerMoore
                .buildLastTable(sellPattern);
        Map<Character, Integer> expectedLastTable = new HashMap<>();
        expectedLastTable.put('s', 0);
        expectedLastTable.put('e', 1);
        expectedLastTable.put('l', 3);
        assertEquals(expectedLastTable, lastTable);
    }

    /**
     * Ensuring LOT is an empty map when the pattern is an empty sequence.
     * JR test.
     */
    @Test (timeout = TIMEOUT)
    public void buildLastTableEmpty() {
        CharSequence sequence = "";
        Map<Character, Integer> result = LastOccurrenceTable.buildLastTable(sequence);
        assertTrue(result.isEmpty());
    }

    /**
     * Testing build LOT when a pattern has no repeated characters.
     * JR test.
     */
    @Test (timeout = TIMEOUT)
    public void buildLastTableAllDistinct() {
        CharSequence sequence = "xyzab123";
        expLastOccTable.put('x', 0);
        expLastOccTable.put('y', 1);
        expLastOccTable.put('z', 2);
        expLastOccTable.put('a', 3);
        expLastOccTable.put('b', 4);
        expLastOccTable.put('1', 5);
        expLastOccTable.put('2', 6);
        expLastOccTable.put('3', 7);
        assertEquals(expLastOccTable, LastOccurrenceTable.buildLastTable(sequence));
    }

    /**
     * Testing build LOT when a pattern has only repeated characters.
     * JR test.
     */
    @Test (timeout = TIMEOUT)
    public void buildLastTableMonochar() {
        CharSequence sequence = "aaaaaaaa";
        expLastOccTable.put('a', 7);
        Map<Character, Integer> res = LastOccurrenceTable.buildLastTable(sequence);
        assertEquals(expLastOccTable, res);
        assertNull(res.get('A'));
        assertNull(res.get('b'));
    }

    /**
     * Testing build LOT when a pattern has only two characters.
     * JR test.
     */
    @Test (timeout = TIMEOUT)
    public void buildLastTableTwoChar() {
        CharSequence sequence = "aaabbaabbabbabbabbba";
        expLastOccTable.put('a', 19);
        expLastOccTable.put('b', 18);
        Map<Character, Integer> res = LastOccurrenceTable.buildLastTable(sequence);
        assertEquals(expLastOccTable, res);
        assertNull(res.get('A'));
        assertNull(res.get('B'));
    }

    /**
     * Testing build LOT when a pattern is cyclic.
     * JR test.
     */
    @Test (timeout = TIMEOUT)
    public void buildLastRepetitious() {
        CharSequence sequence = "abcdefgabcdefg";
        expLastOccTable.put('g', 13);
        expLastOccTable.put('f', 12);
        expLastOccTable.put('e', 11);
        expLastOccTable.put('d', 10);
        expLastOccTable.put('c', 9);
        expLastOccTable.put('b', 8);
        expLastOccTable.put('a', 7);
        Map<Character, Integer> res = LastOccurrenceTable.buildLastTable(sequence);
        assertEquals(expLastOccTable, res);
        assertNull(res.get('A'));
        assertNull(res.get('B'));
    }

    /**
     * Testing build LOT with non-traditional characters.
     * JR test.
     */
    @Test (timeout = TIMEOUT)
    public void buildAllAlphaNumeric() {
        //assumes utf-8 encoding
        //¡¢£¤¥¦§¨©ª«¬­®¯°±²³´µ¶·¸¹º»¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþ !"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}~¡¢£¤¥¦§¨©ª«¬­®¯°±²³´µ¶·¸¹º»¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþÿĀāĂăĄąĆćĈĉĊċČčĎďĐđĒēĔĕĖėĘęĚěĜĝĞğĠġĢģĤĥĦħĨĩĪīĬĭĮįİıĲĳĴĵĶķĸĹĺĻļĽľĿŀŁłŃńŅņŇňŉŊŋŌōŎŏŐőŒœŔŕŖŗŘřŚśŜŝŞşŠšŢţŤťŦŧŨũŪūŬŭŮůŰűŲųŴŵŶŷŸŹźŻżŽžſƀƁƂƃƄƅƆƇƈƉƊƋƌƍƎƏƐƑƒƓƔƕƖƗƘƙƚƛƜƝƞƟƠơƢƣƤƥƦƧƨƩƪƫƬƭƮƯưƱƲƳƴƵƶƷƸƹƺƻƼƽƾƿǀǁǂǃǄǅǆǇǈǉǊǋǌǍǎǏǐǑǒǓǔǕǖǗǘǙǚǛǜǝǞǟǠǡǢǣǤǥǦǧǨǩǪǫǬǭǮǯǰǱǲǳǴǵǶǷǸǹǺǻǼǽǾǿȀȁȂȃȄȅȆȇȈȉȊȋȌȍȎȏȐȑȒȓȔȕȖȗȘșȚțȜȝȞȟȠȡȢȣȤȥȦȧȨȩȪȫȬȭȮȯȰȱȲȳȴȵȶȷȸȹȺȻȼȽȾȿɀɁɂɃɄɅɆɇɈɉɊɋɌɍɎɏɐɑɒɓɔɕɖɗɘəɚɛɜɝɞɟɠɡɢɣɤɥɦɧɨɩɪɫɬɭɮɯɰɱɲɳɴɵɶɷɸɹɺɻɼɽɾɿʀʁʂʃʄʅʆʇʈʉʊʋʌʍʎʏʐʑʒʓʔʕʖʗʘʙʚʛʜʝʞʟʠʡʢʣʤʥʦʧʨʩʪʫʬʭʮʯʰʱʲʳʴʵʶʷʸʹʺʻʼʽʾʿˀˁ˂˃˄˅ˆˇˈˉˊˋˌˍˎˏːˑ˒˓˔˕˖˗˘˙˚˛˜˝˞˟ˠˡˢˣˤ˥˦˧˨˩˪˫ˬ˭ˮ˯˰˱˲˳˴˵˶˷˸˹˺˻˼˽˾˿̴̵̶̷̸̡̢̧̨̛̖̗̘̙̜̝̞̟̠̣̤̥̦̩̪̫̬̭̮̯̰̱̲̳̹̺̻̼͇͈͉͍͎̀́̂̃̄̅̆̇̈̉̊̋̌̍̎̏̐̑̒̓̔̽̾̿̀́͂̓̈́͆͊͋͌̕̚ͅ͏͓͔͕͖͙͚͐͑͒͗͛ͣͤͥͦͧͨͩͪͫͬͭͮͯ͘͜͟͢͝͞͠͡ͰͱͲͳʹ͵Ͷͷ͸͹ͺͻͼͽ;Ϳ΀΁΂΃΄΅Ά·ΈΉΊ΋Ό΍ΎΏΐΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡ΢ΣΤΥΦΧΨΩΪΫάέήίΰαβγδεζηθικλμνξοπρςστυφχψωϊϋόύώϏϐϑϒϓϔϕϖϗϘϙϚϛϜϝϞϟϠϡϢϣϤϥϦϧϨϩϪϫϬϭϮϯϰϱϲϳϴϵ϶ϷϸϹϺϻϼϽϾϿЀЁЂЃЄЅІЇЈЉЊЋЌЍЎЏАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюяѐёђѓєѕіїјљњћќѝўџѠѡѢѣѤѥѦѧѨѩѪѫѬѭѮѯѰѱѲѳѴѵѶѷѸѹѺѻѼѽѾѿҀҁ҂҃҄҅҆҇҈҉ҊҋҌҍҎҏҐґҒғҔҕҖҗҘҙҚқҜҝҞҟҠҡҢңҤҥҦҧҨҩҪҫҬҭҮүҰұҲҳҴҵҶҷҸҹҺһҼҽҾҿӀӁӂӃӄӅӆӇӈӉӊӋӌӍӎӏӐӑӒӓӔӕӖӗӘәӚӛӜӝӞӟӠӡӢӣӤӥӦӧӨөӪӫӬӭӮӯӰӱӲӳӴӵӶӷӸӹӺӻӼӽӾӿԀԁԂԃԄԅԆԇԈԉԊԋԌԍԎԏԐԑԒԓԔԕԖԗԘԙԚԛԜԝԞԟԠԡԢԣԤԥԦԧԨԩԪԫԬԭԮԯ԰ԱԲԳԴԵԶԷԸԹԺԻԼԽԾԿՀՁՂՃՄՅՆՇՈՉՊՋՌՍՎՏՐՑՒՓՔՕՖ՗՘ՙ՚՛
        String p = "";
        for (int i = 161; i < 255; i++) {
            //this loop adds an offset
            p += (char) i;
        }

        for (int i = 32; i < 127; i++) {
            p += (char) i;
            expLastOccTable.put((char) i, 62 + i);
        }

        for (int i = 161; i < 1372; i++) {
            p += (char) i;
            expLastOccTable.put((char) i, i + 28);
        }
        Map<Character, Integer> res = LastOccurrenceTable.buildLastTable(p);
        assertEquals(expLastOccTable, res);
    }

    /**
     * Another non-traditional test.
     * JR test.
     */
    @Test (timeout = TIMEOUT)
    public void buildAllSymbols() {
        String p = "";
        //☭☮☯☰☱☲☳☴☵☶‐‑‒–—―‖‗‘’‚‛“”„‟†‡•‣․‥…‧  ‪‫‬‭‮ ‰‱′″‴‵‶‷‸‹›※‼‽‾‿⁀⁁⁂⁃⁄⁅⁆⁇⁈⁉⁊⁋⁌⁍⁎⁏⁐⁑⁒⁓⁔⁕⁖⁗⁘⁙⁚⁛⁜⁝⁞ ⁠⁡⁢⁣⁤⁥⁦⁧⁨⁩⁪⁫⁬⁭⁮⁯⁰ⁱ⁲⁳⁴⁵⁶⁷⁸⁹⁺⁻⁼⁽⁾ⁿ₀₁₂₃₄₅₆₇₈₉₊₋₌₍₎₏ₐₑₒₓₔₕₖₗₘₙₚₛₜ₝₞₟₠₡₢₣₤₥₦₧₨₩₪₫€₭₮₯₰₱₲₳₴₵₶₷₸₹₺₻₼₽₾₿⃀⃁⃂⃃⃄⃅⃆⃇⃈⃉⃊⃋⃌⃍⃎⃏⃒⃓⃘⃙⃚⃐⃑⃔⃕⃖⃗⃛⃜⃝⃞⃟⃠⃡⃢⃣⃤⃥⃦⃪⃫⃨⃬⃭⃮⃯⃧⃩⃰⃱⃲⃳⃴⃵⃶⃷⃸⃹⃺⃻⃼⃽⃾⃿℀℁ℂ℃℄℅℆ℇ℈℉ℊℋℌℍℎℏℐℑℒℓ℔ℕ№℗℘ℙℚℛℜℝ℞℟℠℡™℣ℤ℥Ω℧ℨ℩KÅℬℭ℮ℯℰℱℲℳℴℵℶℷℸℹ℺℻ℼℽℾℿ⅀⅁⅂⅃⅄ⅅⅆⅇⅈⅉ⅊⅋⅌⅍ⅎ⅏⅐⅑⅒⅓⅔⅕⅖⅗⅘⅙⅚⅛⅜⅝⅞⅟ⅠⅡⅢⅣⅤⅥⅦⅧⅨⅩⅪⅫⅬⅭⅮⅯⅰⅱⅲⅳⅴⅵⅶⅷⅸⅹⅺⅻⅼⅽⅾⅿↀↁↂↃↄↅↆↇↈ↉↊↋↌↍↎↏←↑→↓↔↕↖↗↘↙↚↛↜↝↞↟↠↡↢↣↤↥↦↧↨↩↪↫↬↭↮↯↰↱↲↳↴↵↶↷↸↹↺↻↼↽↾↿⇀⇁⇂⇃⇄⇅⇆⇇⇈⇉⇊⇋⇌⇍⇎⇏⇐⇑⇒⇓⇔⇕⇖⇗⇘⇙⇚⇛⇜⇝⇞⇟⇠⇡⇢⇣⇤⇥⇦⇧⇨⇩⇪⇫⇬⇭⇮⇯⇰⇱⇲⇳⇴⇵⇶⇷⇸⇹⇺⇻⇼⇽⇾⇿∀∁∂∃∄∅∆∇∈∉∊∋∌∍∎∏∐∑−∓∔∕∖∗∘∙√∛∜∝∞∟∠∡∢∣∤∥∦∧∨∩∪∫∬∭∮∯∰∱∲∳∴∵∶∷∸∹∺∻∼∽∾∿≀≁≂≃≄≅≆≇≈≉≊≋≌≍≎≏≐≑≒≓≔≕≖≗≘≙≚≛≜≝≞≟≠≡≢≣≤≥≦≧≨≩≪≫≬≭≮≯≰≱≲≳≴≵≶≷≸≹≺≻≼≽≾≿⊀⊁⊂⊃⊄⊅⊆⊇⊈⊉⊊⊋⊌⊍⊎⊏⊐⊑⊒⊓⊔⊕⊖⊗⊘⊙⊚⊛⊜⊝⊞⊟⊠⊡⊢⊣⊤⊥⊦⊧⊨⊩⊪⊫⊬⊭⊮⊯⊰⊱⊲⊳⊴⊵⊶⊷⊸⊹⊺⊻⊼⊽⊾⊿⋀⋁⋂⋃⋄⋅⋆⋇⋈⋉⋊⋋⋌⋍⋎⋏⋐⋑⋒⋓⋔⋕⋖⋗⋘⋙⋚⋛⋜⋝⋞⋟⋠⋡⋢⋣⋤⋥⋦⋧⋨⋩⋪⋫⋬⋭⋮⋯⋰⋱⋲⋳⋴⋵⋶⋷⋸⋹⋺⋻⋼⋽⋾⋿⌀⌁⌂⌃⌄⌅⌆⌇⌈⌉⌊⌋⌌⌍⌎⌏⌐⌑⌒⌓⌔⌕⌖⌗⌘⌙⌚⌛⌜⌝⌞⌟⌠⌡⌢⌣⌤⌥⌦⌧⌨〈〉⌫⌬⌭⌮⌯⌰⌱⌲⌳⌴⌵⌶⌷⌸⌹⌺⌻⌼⌽⌾⌿⍀⍁⍂⍃⍄⍅⍆⍇⍈⍉⍊⍋⍌⍍⍎⍏⍐⍑⍒⍓⍔⍕⍖⍗⍘⍙⍚⍛⍜⍝⍞⍟⍠⍡⍢⍣⍤⍥⍦⍧⍨⍩⍪⍫⍬⍭⍮⍯⍰⍱⍲⍳⍴⍵⍶⍷⍸⍹⍺⍻⍼⍽⍾⍿⎀⎁⎂⎃⎄⎅⎆⎇⎈⎉⎊⎋⎌⎍⎎⎏⎐⎑⎒⎓⎔⎕⎖⎗⎘⎙⎚⎛⎜⎝⎞⎟⎠⎡⎢⎣⎤⎥⎦⎧⎨⎩⎪⎫⎬⎭⎮⎯⎰⎱⎲⎳⎴⎵⎶⎷⎸⎹⎺⎻⎼⎽⎾⎿⏀⏁⏂⏃⏄⏅⏆⏇⏈⏉⏊⏋⏌⏍⏎⏏⏐⏑⏒⏓⏔⏕⏖⏗⏘⏙⏚⏛⏜⏝⏞⏟⏠⏡⏢⏣⏤⏥⏦⏧⏨⏩⏪⏫⏬⏭⏮⏯⏰⏱⏲⏳⏴⏵⏶⏷⏸⏹⏺⏻⏼⏽⏾⏿␀␁␂␃␄␅␆␇␈␉␊␋␌␍␎␏␐␑␒␓␔␕␖␗␘␙␚␛␜␝␞␟␠␡␢␣␤␥␦␧␨␩␪␫␬␭␮␯␰␱␲␳␴␵␶␷␸␹␺␻␼␽␾␿⑀⑁⑂⑃⑄⑅⑆⑇⑈⑉⑊⑋⑌⑍⑎⑏⑐⑑⑒⑓⑔⑕⑖⑗⑘⑙⑚⑛⑜⑝⑞⑟①②③④⑤⑥⑦⑧⑨⑩⑪⑫⑬⑭⑮⑯⑰⑱⑲⑳⑴⑵⑶⑷⑸⑹⑺⑻⑼⑽⑾⑿⒀⒁⒂⒃⒄⒅⒆⒇⒈⒉⒊⒋⒌⒍⒎⒏⒐⒑⒒⒓⒔⒕⒖⒗⒘⒙⒚⒛⒜⒝⒞⒟⒠⒡⒢⒣⒤⒥⒦⒧⒨⒩⒪⒫⒬⒭⒮⒯⒰⒱⒲⒳⒴⒵ⒶⒷⒸⒹⒺⒻⒼⒽⒾⒿⓀⓁⓂⓃⓄⓅⓆⓇⓈⓉⓊⓋⓌⓍⓎⓏⓐⓑⓒⓓⓔⓕⓖⓗⓘⓙⓚⓛⓜⓝⓞⓟⓠⓡⓢⓣⓤⓥⓦⓧⓨⓩ⓪⓫⓬⓭⓮⓯⓰⓱⓲⓳⓴⓵⓶⓷⓸⓹⓺⓻⓼⓽⓾⓿─━│┃┄┅┆┇┈┉┊┋┌┍┎┏┐┑┒┓└┕┖┗┘┙┚┛├┝┞┟┠┡┢┣┤┥┦┧┨┩┪┫┬┭┮┯┰┱┲┳┴┵┶┷┸┹┺┻┼┽┾┿╀╁╂╃╄╅╆╇╈╉╊╋╌╍╎╏═║╒╓╔╕╖╗╘╙╚╛╜╝╞╟╠╡╢╣╤╥╦╧╨╩╪╫╬╭╮╯╰╱╲╳╴╵╶╷╸╹╺╻╼╽╾╿▀▁▂▃▄▅▆▇█▉▊▋▌▍▎▏▐░▒▓▔▕▖▗▘▙▚▛▜▝▞▟■□▢▣▤▥▦▧▨▩▪▫▬▭▮▯▰▱▲△▴▵▶▷▸▹►▻▼▽▾▿◀◁◂◃◄◅◆◇◈◉◊○◌◍◎●◐◑◒◓◔◕◖◗◘◙◚◛◜◝◞◟◠◡◢◣◤◥◦◧◨◩◪◫◬◭◮◯◰◱◲◳◴◵◶◷◸◹◺◻◼◽◾◿☀☁☂☃☄★☆☇☈☉☊☋☌☍☎☏☐☑☒☓☔☕☖☗☘☙☚☛☜☝☞☟☠☡☢☣☤☥☦☧☨☩☪☫☬☭☮☯☰☱☲☳☴☵☶☷☸☹☺☻☼☽☾☿♀♁♂♃♄♅♆♇♈♉♊♋♌♍♎♏♐♑♒♓♔♕♖♗♘♙♚♛♜♝♞♟♠♡♢♣♤♥♦♧♨♩♪♫♬♭♮♯♰♱♲♳♴♵♶♷♸♹♺♻♼♽♾♿⚀⚁⚂⚃⚄⚅⚆⚇⚈⚉⚊⚋⚌⚍⚎⚏⚐⚑⚒⚓⚔⚕⚖⚗⚘⚙⚚⚛⚜⚝⚞⚟⚠⚡⚢⚣⚤⚥⚦⚧⚨⚩⚪⚫⚬⚭⚮⚯⚰⚱⚲⚳⚴⚵⚶⚷⚸⚹⚺⚻⚼⚽⚾⚿⛀⛁⛂⛃⛄⛅⛆⛇⛈⛉⛊⛋⛌⛍⛎⛏⛐⛑⛒⛓⛔⛕⛖⛗⛘⛙⛚⛛⛜⛝⛞⛟⛠⛡⛢⛣⛤⛥⛦⛧⛨⛩⛪⛫⛬⛭⛮⛯⛰⛱⛲⛳⛴⛵⛶⛷⛸⛹⛺⛻⛼⛽⛾⛿✀✁✂✃✄✅✆✇✈✉✊✋✌✍✎✏✐✑✒✓✔✕✖✗✘✙✚✛✜✝✞✟✠✡✢✣✤✥✦✧✨✩✪✫✬✭✮✯✰✱✲✳✴✵✶✷✸✹✺✻✼✽✾✿❀❁❂❃❄❅❆❇❈❉❊❋❌❍❎❏❐❑❒❓❔❕❖❗❘❙❚❛❜❝❞❟❠❡❢❣❤❥❦❧❨❩❪❫❬❭❮❯❰❱❲❳❴❵❶❷❸❹❺❻❼❽❾❿➀➁➂➃➄➅➆➇➈➉➊➋➌➍➎➏➐➑➒➓➔➕➖➗➘➙➚➛➜➝➞➟➠➡➢➣➤➥➦➧➨➩➪➫➬➭➮➯➰➱➲➳➴➵➶➷➸➹➺➻➼➽➾➿

        //offset loop
        for (int i = 0; i < 10; i++) {
            p += (char) (i + 9773);
            expLastOccTable.put((char) (i + 9773), i);
        }

        for (int i = 8208; i < 10176; i++) {
            p += (char) i;
            expLastOccTable.put((char) i, i  - 8198);
        }
        Map<Character, Integer> res = LastOccurrenceTable.buildLastTable(p);
        assertEquals(expLastOccTable, res);
    }

    /* ------------------------------ fTable Algorithm ---------------------------- */

    // TA Test.
    @Test(timeout = TIMEOUT)
    public void testBuildFailureTable() {
        /*
            pattern: ababa
            failure table: [0, 0, 1, 2, 3]
            comparisons: 4
         */
        int[] failureTable = BoyerMoore
                .buildFailureTable(kmpPattern, comparator);
        int[] expected = {0, 0, 1, 2, 3};
        assertArrayEquals(expected, failureTable);
        assertTrue("Did not use the comparator.",
                comparator.getComparisonCount() != 0);
        assertEquals("Comparison count was " + comparator.getComparisonCount()
                + ". Should be 4.", 4, comparator.getComparisonCount());
    }

    // The following are all JR Tests

    @Test
    public void buildFailureTableEmptyPattern() {
        expFailureTable = new int[0];

        assertArrayEquals(expFailureTable, FailureTable.buildFailureTable("", comparator));
        assertEquals(0, comparator.getComparisonCount());
    }

    @Test
    public void buildFailureTableSingleCharPattern() {
        expFailureTable = new int[] {0};
        assertArrayEquals(expFailureTable, FailureTable.buildFailureTable("A", comparator));
        assertEquals(0, comparator.getComparisonCount());
    }

    @Test
    public void buildFailureTableDistinctCharacters() {
        expFailureTable = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        assertArrayEquals(expFailureTable, FailureTable.buildFailureTable(
                "abcdefghiklmnpoq", comparator)
        );
        assertEquals(15, comparator.getComparisonCount());
    }

    @Test
    public void buildFailureTableRepetitiousNoPrefixes() {
        expFailureTable = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        assertArrayEquals(expFailureTable, FailureTable.buildFailureTable(
                "abccbddbccddccbbcc", comparator)
        );
        assertEquals(17, comparator.getComparisonCount());
    }

    @Test
    public void buildFailureTableNonNestedPrefixes() {
        expFailureTable = new int[] {0, 1, 0, 0, 1, 0, 0, 1, 2, 0, 0, 1, 2, 3, 4, 0, 1, 2, 3, 4, 5, 6, 0};
        assertArrayEquals(expFailureTable, FailureTable.buildFailureTable(
                "aabcadcaadeaabcdaabcade", comparator)
        );

        // a a b c a d c a a d e a a b c d a a b c a d e
        //   _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _
        //           _       _           _             _
        //                   _           _
        assertEquals(28, comparator.getComparisonCount());
    }

    @Test
    public void buildFailureTableNestedPrefixes() {
        expFailureTable = new int[] {0, 1, 0, 0, 1, 2, 2, 2, 3, 4, 5, 6, 3, 4, 5, 6, 7, 8, 0};
        assertArrayEquals(expFailureTable, FailureTable.buildFailureTable(
                "AACGAAAACGAACGAAAAG", comparator)
        );
        // 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18
        // A A C G A A A A C G A  A  C  G  A  A  A  A  G
        //   - - - - - - - - - -  -  -  -  -  -  -  -  -
        //     -       - -           -                 -
        //                                             -


        assertEquals(25, comparator.getComparisonCount());
    }

    @Test
    public void buildFailureTableMonochar() {
        expFailureTable = new int[] {0, 1, 2, 3, 4, 5};
        assertArrayEquals(expFailureTable, FailureTable.buildFailureTable(
                "aaaaaa", comparator)
        );
        // 0 1 2 3 4 5
        // a a a a a a
        //   - - - - -

        assertEquals(5, comparator.getComparisonCount());
    }

    @Test
    public void buildFailureTableRepetitive() {
        expFailureTable = new int[] {0, 0, 0, 0, 0, 1, 2, 3, 4, 1, 2, 3, 1, 2, 3, 1, 0, 0, 1, 2, 3, 1, 2, 1, 2, 3, 4, 1, 2, 1, 2, 3, 1, 1, 2, 1, 1, 1, 2, 3};
        assertArrayEquals(expFailureTable, FailureTable.buildFailureTable(
                "abbccabbcabbabbaccabbababbcababbaabaaabb", comparator)
        );

        assertEquals(52, comparator.getComparisonCount());
    }

    @Test
    public void buildFailureTableNonStandardCharacters() {
        expFailureTable = new int[] {0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 0, 0, 0, 0};
        assertArrayEquals(expFailureTable, FailureTable.buildFailureTable(
                "αμην αμην λεγω", comparator)
        );
        assertEquals(14, comparator.getComparisonCount());
    }

    /* ------------------------------ Search Algorithm ---------------------------- */
    // NEED TO UPDATE SEARCH TESTS SINCE THEY DO NOT ACCOUNT FOR GOOD SUFFIX HEURISTIC

    /**
     * Generic Test.
     * TA test.
     */
    @Test(timeout = TIMEOUT)
    public void testBoyerMooreMatch() {
        /*
            pattern: sell
            text: She sells seashells by the seashore.
            indices: 4
            expected total comparisons: 20
         */
        // Generic BM
//        assertEquals(sellAnswer,
//                BoyerMooreComplete.boyerMoore(sellPattern, sellText, comparator));
//        assertTrue("Did not use the comparator.",
//                comparator.getComparisonCount() != 0);
//        assertEquals("Comparison count was " + comparator.getComparisonCount()
//                + ". Should be 20.", 20, comparator.getComparisonCount());
        // BM Galil
        assertEquals(sellAnswer,
                BoyerMooreComplete.boyerMooreGalil(sellPattern, sellText, comparator));
        assertTrue("Did not use the comparator.",
                comparator.getComparisonCount() != 0);
        assertTrue("Comparison count was " + comparator.getComparisonCount()
                + ". Should be <= 20.", comparator.getComparisonCount() <= 20);
    }

    /**
     * Ensuring that the algorithm correctly identifies when a pattern is not found in the text.
     * TA test.
     */
    @Test(timeout = TIMEOUT)
    public void testBoyerMooreNoMatch() {
        /*
            pattern: sell
            text: sea lions trains cardinal boardwalk
            indices: -
            expected total comparisons: 9
         */
        assertEquals(emptyList,
                BoyerMooreComplete.boyerMooreGalil(sellPattern,
                        sellNoMatch, comparator));
        assertTrue("Did not use the comparator.",
                comparator.getComparisonCount() != 0);
//        assertEquals("Comparison count was " + comparator.getComparisonCount()
//                + ". Should be 9.", 9, comparator.getComparisonCount());
        assertTrue("Comparison count was " + comparator.getComparisonCount()
                + ". Should <= 9.", comparator.getComparisonCount() <= 9);
    }

    /**
     * Ensuring that the algorithm correctly identifies when a pattern is found multiple times in the text.
     * TA test.
     */
    @Test(timeout = TIMEOUT)
    public void testBoyerMooreMultipleMatches() {
        /*
            pattern: ab
            text: abab
            indices: 0, 2
            expected total comparisons: 5
         */
        assertEquals(multipleAnswer,
                BoyerMooreComplete.boyerMooreGalil(multiplePattern,
                        multipleText, comparator));
        assertTrue("Did not use the comparator.",
                comparator.getComparisonCount() != 0);
//        assertEquals("Comparison count was " + comparator.getComparisonCount()
//                + ". Should be 5.", 5, comparator.getComparisonCount());
        assertTrue("Comparison count was " + comparator.getComparisonCount()
                + ". Should <= 9.", comparator.getComparisonCount() <= 5);
    }

    /**
     * Ensuring that the algorithm correctly identifies when a pattern is not found in the text for slightly longer
     * text.
     * TA test.
     */
    @Test(timeout = TIMEOUT)
    public void testBoyerMooreLongerText() {
        /*
            pattern: sea lions trains cardinal boardwalk
            text: sell
            indices: -
            expected total comparisons: 0
         */
        assertEquals(emptyList,
                BoyerMooreComplete.boyerMooreGalil(sellNoMatch,
                        sellPattern, comparator));
        assertEquals(0, comparator.getComparisonCount());
    }

    // THE FOLLOWING ARE ALL JR TESTS

    @Test
    public void boyerMooreBeginningMatch() {
        pattern = "moo";
        text = "moowoofmeowribbet";
        //moo (3)
        //oow (1)
        //oof (1)
        //meo (2)
        //owr (1)
        //ibb (1)
        expMatches.add(0);
        assertEquals(expMatches, BoyerMooreComplete.boyerMooreGalil(pattern, text, comparator));
//        assertEquals(9, comparator.getComparisonCount());
        assertTrue(comparator.getComparisonCount() <= 9);
    }

    @Test
    public void boyerMooreEndMatch() {
        pattern = "meow";
        text = "moowoofribbetmeow";
        //moow (3)
        //oowo (1)
        //owoo (1)
        //woof (1)
        //ribb (1)
        //etme (1)
        //meow (4)
        expMatches.add(13);
        assertEquals(expMatches, BoyerMooreComplete.boyerMooreGalil(pattern, text, comparator));
//        assertEquals(12, comparator.getComparisonCount());
        assertTrue(comparator.getComparisonCount() <= 12);
    }

    @Test
    public void boyerMooreMiddleMatch() {
        pattern = "boo";
        text = "moowoofbooribbetmeow";
        //moo (3)
        //oow (1)
        //oof (1)
        //boo (3)
        //oor (1)
        //ibb (1)
        //bet (1)
        //meo (2)

        expMatches.add(7);
        assertEquals(expMatches, BoyerMooreComplete.boyerMooreGalil(pattern, text, comparator));
//        assertEquals(13, comparator.getComparisonCount());
        assertTrue("Comparison count was " + comparator.getComparisonCount(), comparator.getComparisonCount() <= 13);
    }

    @Test
    // DEBUG
    public void infoForMiddleMatch() {
        int[] shift = new int[4];
        int[] border = new int[4];
        GoodSuffixPreprocessing.preprocessStrongSuffix(shift, border, "boo", new CharacterComparator());
        GoodSuffixPreprocessing.preprocessCase2(shift, border, "boo");
        HashMap<Character, Integer> lot = (HashMap<Character, Integer>) LastOccurrenceTable.buildLastTable("boo");
        for (Character c : lot.keySet()) {
            System.out.print(c + "-> " + lot.get(c) + "|");
        }
        System.out.println();
        System.out.print("shift array: [ " );
        for (int i: shift) {
            System.out.print(i + " ");
        }
        System.out.println(" ]");
        System.out.print("border array: [ ");
        for (int i: border) {
            System.out.print(i + " ");
        }
        System.out.println(" ]");
    }

    @Test
    public void boyerMooreMultiMatch() {
        pattern = "mooo";
        text = "mooowoofmeowmoooomeowmooomeowribbetribbetmoooribbetmooo";
        //mooo (4)
        //ooow (1)
        //oofm (1)
        //meow (1)
        //mooo (4)
        //oooo (4)
        //ooom (1)
        //meow (1)
        //mooo (4)
        //ooom (1)
        //meow (1)
        //ribb (1)
        //etri (1)
        //bbet (1)
        //mooo (4)
        //ooor (1)
        //ibbe (1)
        //tmoo (3)
        //mooo (4)
        expMatches.add(0);
        expMatches.add(12);
        expMatches.add(21);
        expMatches.add(41);
        expMatches.add(51);
        assertEquals(expMatches, BoyerMooreComplete.boyerMooreGalil(pattern, text, comparator));
//        assertEquals(39, comparator.getComparisonCount());
        assertTrue(comparator.getComparisonCount() <= 39);
    }

    @Test
    public void boyerMooreRepeatedMatch() {
        pattern = "moo";
        text = "mooomooomooomoomooomoomoomooomooo";
        //moo (3)
        //ooo (3)
        //oom (1)
        //moo (3)
        //ooo (3)
        //oom (1)
        //moo (3)
        //ooo (3)
        //oom (1)
        //moo (3)
        //oom (1)
        //moo (3)
        //ooo (3)
        //oom (1)
        //moo (3)
        //oom (1)
        //moo (3)
        //oom (1)
        //moo (3)
        //ooo (3)
        //oom (1)
        //moo (3)
        //ooo (3)
        expMatches.add(0);
        expMatches.add(4);
        expMatches.add(8);
        expMatches.add(12);
        expMatches.add(15);
        expMatches.add(19);
        expMatches.add(22);
        expMatches.add(25);
        expMatches.add(29);
        assertEquals(expMatches, BoyerMooreComplete.boyerMooreGalil(pattern, text, comparator));
//        assertEquals(53, comparator.getComparisonCount());
        assertTrue(comparator.getComparisonCount() <= 53);
    }

    @Test
    public void boyerMooreNearMatch() {
        pattern = "abcd";
        text = "cbcdcbcdabcdcbcdcabcdcbcd";
        //cbcd (4)
        //bcdc (1)
        //cdcb (1)
        //cbcd (4)
        //bcda (1)
        //abcd (4)
        //bcdc (1)
        //cdcb (1)
        //cbcd (4)
        //bcdc (1)
        //cdca (1)
        //abcd (4)
        //bcdc (1)
        //cdcb (1)
        //cbcd (4)
        expMatches.add(8);
        expMatches.add(17);
        assertEquals(expMatches, BoyerMooreComplete.boyerMooreGalil(pattern, text, comparator));
//        assertEquals(33, comparator.getComparisonCount());
        assertTrue(comparator.getComparisonCount() <= 33);
    }

    @Test
    public void boyerMooreNoMatch() {
        pattern = "meow";
        text = "The Owl always takes her sleep during the day. Then after sundown, when the rosy light fades from " +
                "the sky and the shadows rise slowly through the wood, out she comes ruffling and blinking from the " +
                "old hollow tree. Now her weird 'hoo-hoo-hoo-oo-oo' echoes through the quiet wood, and she begins " +
                "her hunt for the bugs and beetles, frogs and mice she likes so well to eat.";
        assertEquals(expMatches, BoyerMooreComplete.boyerMooreGalil(pattern, text, comparator));
//        assertEquals(108, comparator.getComparisonCount());
        assertTrue(comparator.getComparisonCount() <= 108);
    }

    @Test
    public void boyerMoorePatternSameText() {
        pattern = "woof";
        text = "woof";
        expMatches.add(0);
        assertEquals(expMatches, BoyerMooreComplete.boyerMooreGalil(pattern, text, comparator));
//        assertEquals(4, comparator.getComparisonCount());
        assertTrue(comparator.getComparisonCount() <= 4);
    }

    @Test
    public void boyerMoorePeriodicPattern() {
        pattern = "abab";
        text = "ababababababcababab";
        //abab (4)
        //baba (1)
        //abab (4)
        //baba (1)
        //abab (4)
        //baba (1)
        //abab (4)
        //baba (1)
        //abab (4)
        //babc (1)
        //abab (4)
        //baba (1)
        //abab (4)
        expMatches.add(0);
        expMatches.add(2);
        expMatches.add(4);
        expMatches.add(6);
        expMatches.add(8);
        expMatches.add(13);
        expMatches.add(15);
        assertEquals(expMatches, BoyerMooreComplete.boyerMooreGalil(pattern, text, comparator));
//        assertEquals(34, comparator.getComparisonCount());
        assertTrue(comparator.getComparisonCount() <= 34);
    }

    @Test
    public void boyerMooreLongerPattern() {
        pattern = "ababababababcababababbababcbabcabcbabcb";
        text = "abab";

        assertEquals(expMatches, BoyerMooreComplete.boyerMooreGalil(pattern, text, comparator));
        assertEquals(0, comparator.getComparisonCount());
    }

    @Test
    public void boyerMooreEmptyText() {
        pattern = "m";
        text = "";

        assertEquals(expMatches, BoyerMooreComplete.boyerMooreGalil(pattern, text, comparator));
        assertEquals(0, comparator.getComparisonCount());
    }

    @Test
    public void boyerMooreSameLength() {
        pattern = "cats";
        text = "scat";

        assertEquals(expMatches, BoyerMooreComplete.boyerMooreGalil(pattern, text, comparator));
        assertEquals(1, comparator.getComparisonCount());
    }

    @Test
    public void boyerMooreSingleChar() {
        pattern = "a";
        text = "aFar away and long ago, off in a dark forest, lived a peaceful little witch named 'Ruston'.a";
        expMatches.add(0);
        expMatches.add(2);
        expMatches.add(5);
        expMatches.add(7);
        expMatches.add(10);
        expMatches.add(19);
        expMatches.add(31);
        expMatches.add(34);
        expMatches.add(52);
        expMatches.add(56);
        expMatches.add(77);
        expMatches.add(91);
        assertEquals(expMatches, BoyerMooreComplete.boyerMooreGalil(pattern, text, comparator));
//        assertEquals(92, comparator.getComparisonCount());
        assertTrue(comparator.getComparisonCount() <= 92);
    }

    @Test
    public void boyerMooreLongText() {
        pattern = "key";
        text = "Once upon a time all the crows in a town called Mahilaropya made a huge banyan tree their home. The tree\n" +
                "had hundreds of branches. Their king, known as Meghavarna, set up strong fortifications to ensure security\n" +
                "for his brood. Similarly, the owls of the town made a nearby cave their colony. They also had a king, called\n" +
                "Arimardana, who ruled with the help of a strong and cunning army.\n" +
                "The owl king kept a close eye on the banyan tree and on account of previous enmity killed every night any\n" +
                "crow he sighted outside the tree. Slowly, the owl king managed to kill all crows that could be seen outside\n" +
                "the tree. That is why wise men had always said that whoever neglects disease or the enemy perishes in their\n" +
                "hands.\n" +
                "Alarmed at the loss of his flock, Meghavarna assembled his ministers and asked them to prepare a plan to\n" +
                "fight the owls. He placed before them six strategies and asked them to name the best of the six. The first\n" +
                "minister suggested compromise as a tactic because one had first to survive to gather strength and later destroy\n" +
                "the enemy. The elders have said,\n" +
                "“Bend to the enemy when he is strong\n" +
                "Attack him when he is vulnerable.\n" +
                "Don’t wage a war if it doesn’t bring\n" +
                "Power, or wealth or friendship.”\n" +
                "The second minister ruled out compromise and offered trickery as a formula. He cited the example of how\n" +
                "Bheema in the Mahabharata had killed Keechaka in the disguise of a woman. He also quoted elders saying,\n" +
                "“Never accept peace with\n" +
                "An enemy who is not just\n" +
                "For, he will break his word\n" +
                "And stab you in the back.”\n" +
                "The minister referred to the learned as saying that it is easy to defeat an enemy who is a tyrant, a miser, an\n" +
                "idler, a liar, a coward and a fool. Words of peace will only inflame an enemy blinded by anger.\n" +
                "The third minister said, “O lord, our enemy is not only strong but also wicked. Neither compromise nor\n" +
                "trickery will work with him. Exile is the best way. We shall wait and strike when the enemy becomes weak.”\n" +
                "“Neither peace nor bravado\n" +
                "Can subdue a strong enemy\n" +
                "Where these two do not work\n" +
                "Flight is the best alternative.”\n" +
                "The fourth minister opposed all these tactics and suggested the king of crows should stay in his own fort,\n" +
                "mobilize support from friends and then attack the enemy. He quoted the learned as saying,\n" +
                "“A king who flees is like\n" +
                "A cobra without fangs.\n" +
                "A crocodile in water\n" +
                "Can haul an elephant.”\n" +
                "Therefore, the minister said, “An ally is what wind is to fire. The king must stay where he is and gather allies\n" +
                "for support.”\n" +
                "The fifth minister offered a strategy similar to that of the fourth and said, “Stay in your fort and seek the help\n" +
                "of an ally stronger than the enemy. It also pays to form an axis of less strong allies.”\n" +
                "After listening to all the ministers, Meghavarna turned to the wisest and senior most among his counsels,\n" +
                "Sthirajeevi, and asked him for his advice. The wise man told Meghavarna,\n" +
                "“Oh, king of crows, this is the time to use duplicity to finish the enemy. You can thus keep your throne.”\n" +
                "“But learned sir, we have no idea of where Arimardana lives and of what his failings are.”\n" +
                "“That is not difficult. Send your spies and gather information on the key men advising the king of owls. The\n" +
                "next step is to divide them by setting one against the other.”\n" +
                "“Tell me why did the crows and owls fall out in the first place,” asked Meghavarna.\n" +
                "Sthirajeevi said, “That is another story. Long, long ago all the birds in the jungle—swans, parrots, cranes,\n" +
                "nightingales, owls, peacocks, pigeons, pheasants, sparrows, crows etc.—assembled and expressed anguish\n" +
                "that their king Garuda had become indifferent to their welfare and failed to save them from poachers.\n" +
                "Believing that people without a protector were like passengers in a ship without a captain, they decided to\n" +
                "elect a new king. They chose an owl as their king.\n" +
                "As the owl was being crowned, a crow flew into the assembly and asked them why and what they were\n" +
                "celebrating. When the birds told him the details, the crow told them, the owl is a wicked and ugly bird and it\n" +
                "is unwise to choose another leader when Garuda is still alive. To crush enemies it is enough if you mentioned\n" +
                "Garuda’s name or for that matter the name of anyone who is great. That was how the hares managed to live\n" +
                "happily by taking the name of the moon.”\n" +
                "The birds asked the visiting crow, “Tell us how this has happened.”\n" +
                "“I will tell you,” said the crow and began telling them the story of the hares and the elephants.";
        expMatches.add(3126);
        assertEquals(expMatches, BoyerMooreComplete.boyerMooreGalil(pattern, text, comparator));
//        assertEquals(1606, comparator.getComparisonCount());
        assertTrue(comparator.getComparisonCount() <= 1606);
    }

    @Test
    public void boyerMooreNonAlphaNumeric() {
        pattern = "☮";
        text = "✨✩✪✫✬✭✮✯✰✱✲✳✴✵✶✷✸☮✹✺✻✼✽✾✿❀❁❂❃❄❅❆❇❈❉❊❋❌❍❎❏❐❑❒❓❔❕❖❗❘❙❚❛❜❝❞❟❠❡❢❣❤❥❦❧";
        expMatches.add(17);
        assertEquals(expMatches, BoyerMooreComplete.boyerMooreGalil(pattern, text, comparator));
//        assertEquals(65, comparator.getComparisonCount());
        assertTrue(comparator.getComparisonCount() <= 65);
    }

    @Test()
    public void boyerMooreNoBackwardShift() {
        // This test is designed for the situation where
        // we do NOT move the pattern to align with the last occurrence
        // because doing so would cause the pattern to move backwards

        //P: a b a c b a b a d c a b a c a b
        //   a b a c a b
        //             -
        //     a b a c a b                            (don't align LOT of 'b' with the failure here, as doing so would
        //           - - -                             move backward)
        //       a b a c a b                          (instead, move pattern forward only once)
        //                 -
        //         a b a c a b
        //                   -
        //P: a b a c b a b a d c a b a c a b         (repeated so it's not a strain to keep looking up)
        //                     a b a c a b
        //                               -
        //                       a b a c a b
        //                       - - - - - -

        // Count up all the dashes: 13 comparisons

        CharSequence text = "abacbabadcabacab";
        CharSequence pattern = "abacab";
        expMatches.add(10);
        assertEquals(expMatches, BoyerMooreComplete.boyerMooreGalil(pattern, text, comparator));
//        assertEquals(13, comparator.getComparisonCount());
        assertTrue("Comparison count was " + comparator.getComparisonCount(), comparator.getComparisonCount() <= 13);
    }

    @Test
    public void boyerMooreLongPattern() {
        pattern = "key men advising the king of owls. The\n" +
                "next step is to divide them by setting one against the other.”\n" +
                "“Tell me why did the crows and owls fall out in the first place,” asked Meghavarna.\n";

        text = "Once upon a time all the crows in a town called Mahilaropya made a huge banyan tree their home. The tree\n" +
                "had hundreds of branches. Their king, known as Meghavarna, set up strong fortifications to ensure security\n" +
                "for his brood. Similarly, the owls of the town made a nearby cave their colony. They also had a king, called\n" +
                "Arimardana, who ruled with the help of a strong and cunning army.\n" +
                "The owl king kept a close eye on the banyan tree and on account of previous enmity killed every night any\n" +
                "crow he sighted outside the tree. Slowly, the owl king managed to kill all crows that could be seen outside\n" +
                "the tree. That is why wise men had always said that whoever neglects disease or the enemy perishes in their\n" +
                "hands.\n" +
                "Alarmed at the loss of his flock, Meghavarna assembled his ministers and asked them to prepare a plan to\n" +
                "fight the owls. He placed before them six strategies and asked them to name the best of the six. The first\n" +
                "minister suggested compromise as a tactic because one had first to survive to gather strength and later destroy\n" +
                "the enemy. The elders have said,\n" +
                "“Bend to the enemy when he is strong\n" +
                "Attack him when he is vulnerable.\n" +
                "Don’t wage a war if it doesn’t bring\n" +
                "Power, or wealth or friendship.”\n" +
                "The second minister ruled out compromise and offered trickery as a formula. He cited the example of how\n" +
                "Bheema in the Mahabharata had killed Keechaka in the disguise of a woman. He also quoted elders saying,\n" +
                "“Never accept peace with\n" +
                "An enemy who is not just\n" +
                "For, he will break his word\n" +
                "And stab you in the back.”\n" +
                "The minister referred to the learned as saying that it is easy to defeat an enemy who is a tyrant, a miser, an\n" +
                "idler, a liar, a coward and a fool. Words of peace will only inflame an enemy blinded by anger.\n" +
                "The third minister said, “O lord, our enemy is not only strong but also wicked. Neither compromise nor\n" +
                "trickery will work with him. Exile is the best way. We shall wait and strike when the enemy becomes weak.”\n" +
                "“Neither peace nor bravado\n" +
                "Can subdue a strong enemy\n" +
                "Where these two do not work\n" +
                "Flight is the best alternative.”\n" +
                "The fourth minister opposed all these tactics and suggested the king of crows should stay in his own fort,\n" +
                "mobilize support from friends and then attack the enemy. He quoted the learned as saying,\n" +
                "“A king who flees is like\n" +
                "A cobra without fangs.\n" +
                "A crocodile in water\n" +
                "Can haul an elephant.”\n" +
                "Therefore, the minister said, “An ally is what wind is to fire. The king must stay where he is and gather allies\n" +
                "for support.”\n" +
                "The fifth minister offered a strategy similar to that of the fourth and said, “Stay in your fort and seek the help\n" +
                "of an ally stronger than the enemy. It also pays to form an axis of less strong allies.”\n" +
                "After listening to all the ministers, Meghavarna turned to the wisest and senior most among his counsels,\n" +
                "Sthirajeevi, and asked him for his advice. The wise man told Meghavarna,\n" +
                "“Oh, king of crows, this is the time to use duplicity to finish the enemy. You can thus keep your throne.”\n" +
                "“But learned sir, we have no idea of where Arimardana lives and of what his failings are.”\n" +
                "“That is not difficult. Send your spies and gather information on the key men advising the king of owls. The\n" +
                "next step is to divide them by setting one against the other.”\n" +
                "“Tell me why did the crows and owls fall out in the first place,” asked Meghavarna.\n" +
                "Sthirajeevi said, “That is another story. Long, long ago all the birds in the jungle—swans, parrots, cranes,\n" +
                "nightingales, owls, peacocks, pigeons, pheasants, sparrows, crows etc.—assembled and expressed anguish\n" +
                "that their king Garuda had become indifferent to their welfare and failed to save them from poachers.\n" +
                "Believing that people without a protector were like passengers in a ship without a captain, they decided to\n" +
                "elect a new king. They chose an owl as their king.\n" +
                "As the owl was being crowned, a crow flew into the assembly and asked them why and what they were\n" +
                "celebrating. When the birds told him the details, the crow told them, the owl is a wicked and ugly bird and it\n" +
                "is unwise to choose another leader when Garuda is still alive. To crush enemies it is enough if you mentioned\n" +
                "Garuda’s name or for that matter the name of anyone who is great. That was how the hares managed to live\n" +
                "happily by taking the name of the moon.”\n" +
                "The birds asked the visiting crow, “Tell us how this has happened.”\n" +
                "“I will tell you,” said the crow and began telling them the story of the hares and the elephants.";

        expMatches.add(3126);
        assertEquals(expMatches, BoyerMooreComplete.boyerMooreGalil(pattern, text, comparator));
//        assertEquals(393, comparator.getComparisonCount());
        assertTrue(comparator.getComparisonCount() <= 393);
    }

    // was just using this and changing the pattern to build failure tables that could break the implementation
//    @Test(timeout = TIMEOUT)
//    public void weirdPeriodicityTest() {
//        CharSequence pattern = "abcabcabcab";
//        int[] ftable = BoyerMooreComplete.buildFailureTable(pattern, comparator);
//        for (int a : ftable) {
//            System.out.print(a + ", ");
//        }
//        System.out.println();
//    }

//    @Test(timeout = TIMEOUT)
//    public void firstGalilTest() {
//        /*
//            text: abababaaabababa
//            pattern: ababa
//            pattern periodicity: 2
//         */
//        CharSequence text = "abababaaabababa";
//        CharSequence pattern = "ababa";
//        int k = pattern.length() - BoyerMooreComplete.buildFailureTable(pattern, comparator)[pattern.length()-1];
//        // Testing periodicity
//        assertEquals(2, k);
//        ArrayList<Integer> matches = (ArrayList<Integer>) BoyerMooreComplete.boyerMooreGalil(pattern, text, comparator);
//        ArrayList<Integer> correct = new ArrayList<>();
//        correct.add(0);
//        correct.add(2);
//        correct.add(8);
//        correct.add(10);
//        assertEquals(correct, matches);
//    }
//
//    @Test(timeout = TIMEOUT)
//    public void GalilTestOne() {
//        CharSequence text = "abababaaabababa";
//        CharSequence pattern = "ababa";
//        int k = pattern.length() - BoyerMooreComplete.buildFailureTable(pattern, comparator)[pattern.length() -1];
//    }

}
