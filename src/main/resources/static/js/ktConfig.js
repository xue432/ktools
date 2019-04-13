/**
 * js全局配置文件
 * @type {{logEnable: boolean}}
 */
var kfc = ktcfg.ctx;
var ktConfig = {
    menuId: null,
    userId: null,
    nickname: null,
    logEnable: true,    // 日志开关
    request: {  // 请求相关
        okCode: 200,
        failCode: 400
    },
    suggestFontArr: ['standard', 'starwars', 'alpha', 'bear', 'big', 'bigfig', 'block', 'graceful', 'lean'],    // 建议字体
    allFontArr: ['1943____', '1row', '3-d', '3d_diagonal', '3x5', '4max', '4x4_offr', '5lineoblique', '5x7', '5x8', '64f1____', '6x10', '6x9', 'acrobatic', 'advenger', 'alligator', 'alligator2', 'alligator3', 'alpha', 'alphabet', 'amc3line', 'amc3liv1', 'amcaaa01', 'amcneko', 'amcrazo2', 'amcrazor', 'amcslash', 'amcslder', 'amcthin', 'amctubes', 'amcun1', 'aquaplan', 'arrows', 'ascii_new_roman', 'ascii___', 'asc_____', 'assalt_m', 'asslt__m', 'atc_gran', 'atc_____', 'avatar', 'a_zooloo', 'B1FF', 'banner', 'banner3-D', 'banner3', 'banner4', 'barbwire', 'basic', 'battlesh', 'battle_s', 'baz__bil', 'bear', 'beer_pub', 'bell', 'benjamin', 'big', 'bigchief', 'bigfig', 'binary', 'block', 'blocks', 'bolger', 'braced', 'bright', 'brite', 'briteb', 'britebi', 'britei', 'broadway', 'broadway_kb', 'bubble', 'bubble_b', 'bubble__', 'bulbhead', 'b_m__200', 'c1______', 'c2______', 'calgphy2', 'caligraphy', 'cards', 'catwalk', 'caus_in_', 'char1___', 'char2___', 'char3___', 'char4___', 'charact1', 'charact2', 'charact3', 'charact4', 'charact5', 'charact6', 'characte', 'charset_', 'chartr', 'chartri', 'chiseled', 'chunky', 'clb6x10', 'clb8x10', 'clb8x8', 'cli8x8', 'clr4x6', 'clr5x10', 'clr5x6', 'clr5x8', 'clr6x10', 'clr6x6', 'clr6x8', 'clr7x10', 'clr7x8', 'clr8x10', 'clr8x8', 'coil_cop', 'coinstak', 'cola', 'colossal', 'computer', 'com_sen_', 'contessa', 'contrast', 'convoy__', 'cosmic', 'cosmike', 'cour', 'courb', 'courbi', 'couri', 'crawford', 'crazy', 'cricket', 'cursive', 'cyberlarge', 'cybermedium', 'cybersmall', 'cygnet', 'c_ascii_', 'c_consen', 'DANC4', 'dancingfont', 'dcs_bfmo', 'decimal', 'deep_str', 'defleppard', 'demo_1__', 'demo_2__', 'demo_m__', 'devilish', 'diamond', 'dietcola', 'digital', 'doh', 'doom', 'dosrebel', 'dotmatrix', 'double', 'doubleshorts', 'drpepper', 'druid___', 'dwhistled', 'd_dragon', 'ebbs_1__', 'ebbs_2__', 'eca_____', 'eftichess', 'eftifont', 'eftipiti', 'eftirobot', 'eftitalic', 'eftiwall', 'eftiwater', 'epic', 'etcrvs__', 'e__fist_', 'f15_____', 'faces_of', 'fairligh', 'fair_mea', 'fantasy_', 'fbr12___', 'fbr1____', 'fbr2____', 'fbr_stri', 'fbr_tilt', 'fender', 'filter', 'finalass', 'fireing_', 'fire_font-k', 'fire_font-s', 'flipped', 'flowerpower', 'flyn_sh', 'fourtops', 'fp1_____', 'fp2_____', 'fraktur', 'funface', 'funfaces', 'funky_dr', 'future_1', 'future_2', 'future_3', 'future_4', 'future_5', 'future_6', 'future_7', 'future_8', 'fuzzy', 'gauntlet', 'georgi16', 'georgia11', 'ghost', 'ghost_bo', 'ghoulish', 'glenyn', 'goofy', 'gothic', 'gothic__', 'graceful', 'gradient', 'graffiti', 'grand_pr', 'greek', 'green_be', 'hades___', 'heart_left', 'heart_right', 'heavy_me', 'helv', 'helvb', 'helvbi', 'helvi', 'henry3d', 'heroboti', 'hex', 'hieroglyphs', 'high_noo', 'hills___', 'hollywood', 'home_pak', 'horizontalleft', 'horizontalright', 'house_of', 'hypa_bal', 'hyper___', 'ICL-1900', 'impossible', 'inc_raw_', 'invita', 'isometric1', 'isometric2', 'isometric3', 'isometric4', 'italic', 'italics_', 'ivrit', 'jacky', 'jazmine', 'jerusalem', 'joust___', 'katakana', 'kban', 'keyboard', 'kgames_i', 'kik_star', 'knob', 'konto', 'kontoslant', 'krak_out', 'larry3d', 'lazy_jon', 'lcd', 'lean', 'letters', 'letterw3', 'letter_w', 'lexible_', 'lildevil', 'lineblocks', 'linux', 'lockergnome', 'madrid', 'mad_nurs', 'magic_ma', 'marquee', 'master_o', 'maxfour', 'mayhem_d', 'mcg_____', 'merlin1', 'merlin2', 'mig_ally', 'mike', 'mini', 'mirror', 'mnemonic', 'modern__', 'modular', 'morse', 'morse2', 'moscow', 'mshebrew210', 'muzzle', 'nancyj-fancy', 'nancyj-improved', 'nancyj-underlined', 'nancyj', 'new_asci', 'nfi1____', 'nipples', 'notie_ca', 'npn_____', 'nscript', 'ntgreek', 'nvscript', 'o8', 'octal', 'odel_lak', 'ogre', 'ok_beer_', 'oldbanner', 'os2', 'outrun__', 'pacos_pe', 'panther_', 'pawn_ins', 'pawp', 'peaks', 'peaksslant', 'pebbles', 'pepper', 'phonix__', 'platoon2', 'platoon_', 'pod_____', 'poison', 'puffy', 'puzzle', 'pyramid', 'p_skateb', 'p_s_h_m_', 'r2-d2___', 'radical_', 'rad_phan', 'rad_____', 'rainbow_', 'rally_s2', 'rally_sp', 'rammstein', 'rampage_', 'rastan__', 'raw_recu', 'rci_____', 'rectangles', 'red_phoenix', 'relief', 'relief2', 'rev', 'reverse', 'ripper!_', 'road_rai', 'rockbox_', 'rok_____', 'roman', 'roman___', 'rot13', 'rot13', 'rotated', 'rounded', 'rowancap', 'rozzo', 'runic', 'runyc', 's-relief', 'sans', 'sansb', 'sansbi', 'sansi', 'santaclara', 'sblood', 'sbook', 'sbookb', 'sbookbi', 'sbooki', 'script', 'script__', 'serifcap', 'shadow', 'shimrod', 'short', 'skateord', 'skateroc', 'skate_ro', 'sketch_s', 'slant', 'slide', 'slscript', 'small', 'smallcaps', 'smisome1', 'smkeyboard', 'smpoison', 'smscript', 'smshadow', 'smslant', 'smtengwar', 'sm______', 'soft', 'space_op', 'spc_demo', 'speed', 'spliff', 'stacey', 'stampate', 'stampatello', 'standard', 'starstrips', 'starwars', 'star_war', 'stealth_', 'stellar', 'stencil1', 'stencil2', 'stforek', 'stop', 'straight', 'street_s', 'sub-zero', 'subteran', 'super_te', 'swampland', 'swan', 'sweet', 'tanja', 'tav1____', 'taxi____', 'tec1____', 'tecrvs__', 'tec_7000', 'tengwar', 'term', 'test1', 'thick', 'thin', 'threepoint', 'ticks', 'ticksslant', 'tiles', 'times', 'timesofl', 'tinker-toy', 'ti_pan__', 'tomahawk', 'tombstone', 'top_duck', 'train', 'trashman', 'trek', 'triad_st', 'ts1_____', 'tsalagi', 'tsm_____', 'tsn_base', 'tty', 'ttyb', 'tubular', 'twin_cob', 'twisted', 'twopoint', 'type_set', 't__of_ap', 'ucf_fan_', 'ugalympi', 'unarmed_', 'univers', 'usaflag', 'usa_pq__', 'usa_____', 'utopia', 'utopiab', 'utopiabi', 'utopiai', 'varsity', 'vortron_', 'war_of_w', 'wavy', 'weird', 'wetletter', 'whimsy', 'wow', 'xbrite', 'xbriteb', 'xbritebi', 'xbritei', 'xchartr', 'xchartri', 'xcour', 'xcourb', 'xcourbi', 'xcouri', 'xhelv', 'xhelvb', 'xhelvbi', 'xhelvi', 'xsans', 'xsansb', 'xsansbi', 'xsansi', 'xsbook', 'xsbookb', 'xsbookbi', 'xsbooki', 'xtimes', 'xtty', 'xttyb', 'yie-ar__', 'yie_ar_k', 'z-pilot_', 'zig_zag_', 'zone7___'], // 所有字体
    defaultTPic: '/static/image/default_t_red.png',
    navTabs:{   // 页签对象
        image:[ // 图片工具页签数组
            {name: '图片转换艺术图', url: '/image/artPic', pic: '/static/image/ktpysj.png', key: 1},
            {name: '图片转换字符画', url: '/image/asciiPic', pic: '/static/image/ktzfh.png', key: 2},
            {name: 'GIF转换AsciiGif', url: '/image/asciiGif', pic: '/static/image/ascgif.gif', key: 3},
            {name: '图片转换彩色Ascii图', url: '/image/colorAsciiPic', pic: '/static/image/clascpic.png', key: 4},
            {name: '图片合成GIF动图', url: '/image/gif', pic: '/static/image/cpgif.gif', key: 5}
        ],
        txt:[   // 文字工具页签数组
            {name: '文字转换ascii艺术字', url: '/txt/ascii', pic: '/static/image/ktysj.png', key: 1}
        ],
        head:[  // 头像相关页签数组
            {name: '头像大全', url: '/head', pic: '', key: 1},
            {name: '生成头像', url: '/head/pixel', pic: '/static/image/ktysj.png', key: 2}
        ]
    },
    moduleType: {
        image: 'IMAGE',
        txt: 'TXT',
        head: 'HEAD',
        convenience: 'CONVENIENCE', // 便民
        dev: 'DEV',
        other: 'OTHER'

    },
    browserType: {
        chrome: 'Chrome',
        ie: 'ie',
        edg: 'edg',
        firefox: 'Firefox'
    },
    api: {  // 网站数据接口api
        imageUpload: kfc + '/image/upload',
        imageUploadAndHandle: kfc + '/image/uploadAndHandle',
        imageHandle: kfc + '/image/handle',
        imageDownload: kfc + '/image/download',
        imageAscii: kfc + '/image/to/ascii',
        txtAscii: kfc + '/txt/to/ascii',
        image2Gif: kfc + '/image/to/gif',
        gif2AsciiGif: kfc + '/image/gif/2AsciiGif',
        image2ColorAsciiPic: kfc + '/image/to/colorAsciiPic',
        imageAddWatermark: kfc + '/image/add/watermark',
        video2AsciiGif: kfc + '/video/to/asciiGif',
        allMenus: kfc + '/menu/all',
        stressTesting: kfc + '/dev/stressTesting',
        getIpInfo: kfc + '/convenience/get/ipInfo',
        qrCode2Image: kfc + '/convenience/qrCode/2Image',
        linuxCmdCategory: kfc + '/dev/linuxCmd/category',
        linuxCmdQuery: kfc + '/dev/linuxCmd/query',
        countToolsView: kfc + '/trafficRecords/count/toolsView',
        shakedown12306: kfc + '/convenience/shakedown',
        userComments: kfc + '/comment/userComments',
        addComment: kfc + '/comment/add',
        giveLikeTool: kfc + '/toolLike/giveLike',
        dislikeTool: kfc + '/toolLike/dislike',
        giveLikeComment: kfc + '/comment/giveLike',
        dislikeComment: kfc + '/comment/dislike'

    }
};