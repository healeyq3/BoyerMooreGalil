import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
//import static org.junit.Assert.assertArrayEquals;

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
    private CharacterComparator comparatorG;

    @Before
    public void setUp() {
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
        comparatorG = new CharacterComparator();
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
        Map<Character, Integer> result = BoyerMoore.buildLastTable(sequence);
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
        assertEquals(expLastOccTable, BoyerMoore.buildLastTable(sequence));
    }

    /**
     * Testing build LOT when a pattern has only repeated characters.
     * JR test.
     */
    @Test (timeout = TIMEOUT)
    public void buildLastTableMonochar() {
        CharSequence sequence = "aaaaaaaa";
        expLastOccTable.put('a', 7);
        Map<Character, Integer> res = BoyerMoore.buildLastTable(sequence);
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
        Map<Character, Integer> res = BoyerMoore.buildLastTable(sequence);
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
        Map<Character, Integer> res = BoyerMoore.buildLastTable(sequence);
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
        Map<Character, Integer> res = BoyerMoore.buildLastTable(p);
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
        Map<Character, Integer> res = BoyerMoore.buildLastTable(p);
        assertEquals(expLastOccTable, res);
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
        assertEquals(sellAnswer,
                BoyerMoore.boyerMoore(sellPattern, sellText, comparator));
        assertTrue("Did not use the comparator.",
                comparator.getComparisonCount() != 0);
        assertEquals("Comparison count was " + comparator.getComparisonCount()
                + ". Should be 20.", 20, comparator.getComparisonCount());
        // BM Galil
        assertEquals(sellAnswer,
                BoyerMoore.boyerMooreGalil(sellPattern, sellText, comparatorG));
        assertTrue("Did not use the comparator.",
                comparatorG.getComparisonCount() != 0);
        assertEquals("Comparison count was " + comparatorG.getComparisonCount()
                + ". Should be 20.", 20, comparatorG.getComparisonCount());
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
                BoyerMoore.boyerMoore(sellPattern,
                        sellNoMatch, comparator));
        assertTrue("Did not use the comparator.",
                comparator.getComparisonCount() != 0);
        assertEquals("Comparison count was " + comparator.getComparisonCount()
                + ". Should be 9.", 9, comparator.getComparisonCount());
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
                BoyerMoore.boyerMoore(multiplePattern,
                        multipleText, comparator));
        assertTrue("Did not use the comparator.",
                comparator.getComparisonCount() != 0);
        assertEquals("Comparison count was " + comparator.getComparisonCount()
                + ". Should be 5.", 5, comparator.getComparisonCount());
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
                BoyerMoore.boyerMoore(sellNoMatch,
                        sellPattern, comparator));
        assertEquals(0, comparator.getComparisonCount());
    }

    @Test (timeout = TIMEOUT)
    public void firstGalilTest() {
        /*
            text: abababaaabababa
            pattern: ababa
            pattern periodicity: 2

         */
        CharSequence text = "abababaaabababa";
        CharSequence pattern = "ababa";
        // Testing periodicity
        assertEquals(2, pattern.length() - BoyerMoore.buildFailureTable(pattern, comparator)[pattern.length()-1]);
        ArrayList<Integer> answer = (ArrayList<Integer>) BoyerMoore.boyerMooreGalil(pattern, text, comparator);
        System.out.println("Answer size: " + answer.size());
        for (int a : answer) {
            System.out.print(a + ", ");
        }
        System.out.println();
        // This correctly outputted: 0, 2, 8, 10
        // Will add more proper tests and work on completing commenting/explanations/cleaning tomorrow (12/28)

    }

    // was just using this and changing the pattern to build failure tables that could break the implementation
    @Test(timeout = TIMEOUT)
    public void weirdPeriodicityTest() {
        CharSequence pattern = "abcabcabcab";
        int[] ftable = BoyerMoore.buildFailureTable(pattern, comparator);
        for (int a : ftable) {
            System.out.print(a + ", ");
        }
        System.out.println();
    }

}
