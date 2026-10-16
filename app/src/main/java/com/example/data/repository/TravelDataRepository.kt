package com.example.data.repository

import com.example.data.model.CountryEtiquette
import com.example.data.model.Language
import com.example.data.model.TravelPhrase
import com.example.data.model.TravelScenario

object TravelDataRepository {

    val COUNTRY_ETIQUETTES = listOf(
        CountryEtiquette(
            country = "Japan",
            flag = "🇯🇵",
            languageCode = "ja",
            greetings = "Konnichiwa (Hello) with a subtle 15-30° bow. Avoid handshakes unless initiated.",
            tippingCustom = "No tipping whatsoever. Tipping can be seen as confusing or disrespectful.",
            diningRules = listOf(
                "Never stick chopsticks upright in a bowl of rice (associated with funerals).",
                "Slurping noodles (ramen/soba) is welcomed as a sign of appreciation and cooling the soup.",
                "Say 'Itadakimasu' before eating and 'Gochisousama deshita' when finishing."
            ),
            etiquetteTips = listOf(
                "Keep voice low on trains and subways; take phone calls on the platform, not inside the car.",
                "Take off shoes when stepping onto tatami mats or inside fitting rooms/homes.",
                "Carry cash (1,000 yen notes) as small ramen shops and coin lockers still prefer cash."
            ),
            taboos = listOf(
                "Walking while eating street food (eat near the stall or vending machine).",
                "Blowing your nose loudly in public.",
                "Passing food directly from chopstick to chopstick."
            ),
            emergencyNumbers = "Police: 110 | Ambulance & Fire: 119"
        ),
        CountryEtiquette(
            country = "France",
            flag = "🇫🇷",
            languageCode = "fr",
            greetings = "Always say 'Bonjour Madame/Monsieur' immediately when entering any shop, bakery, or cafe.",
            tippingCustom = "Service charge ('service compris') is included in the bill. Leaving 1-2€ in coins is polite.",
            diningRules = listOf(
                "Keep both hands above the table while eating (wrists on the table, not elbows).",
                "Do not ask for ice in wine or heavy modifications to a dish unless medically necessary.",
                "Bread is placed directly on the tablecloth beside your plate, not on the dinner plate."
            ),
            etiquetteTips = listOf(
                "Politeness is prized: failing to say 'Bonjour' before asking a question is considered rude.",
                "Keep voices moderate inside bistros and on the metro.",
                "Metro validation tickets must be kept until exiting the station."
            ),
            taboos = listOf(
                "Snapping fingers or shouting to call the waiter (make eye contact or subtle nod).",
                "Asking for a doggy bag in high-end gastronomic restaurants."
            ),
            emergencyNumbers = "European Emergency: 112 | Police: 17 | SAMU (Medical): 15"
        ),
        CountryEtiquette(
            country = "Italy",
            flag = "🇮🇹",
            languageCode = "it",
            greetings = "Buongiorno (day) or Buonasera (evening). Ciao is reserved for friends and youth.",
            tippingCustom = "A 'coperto' (cover charge) is normal on the bill. Rounding up a few euros is appreciated.",
            diningRules = listOf(
                "Cappuccino is only consumed in the morning before 11:00 AM, never after a heavy meal or with pasta.",
                "Never ask for parmesan cheese on seafood pasta.",
                "Twirling spaghetti with a spoon is frowned upon by locals; use only the fork against the plate curve."
            ),
            etiquetteTips = listOf(
                "Drink espresso standing up at the 'banco' (bar counter) for local price, sitting at tables costs more.",
                "Cover shoulders and knees when visiting basilicas and churches (e.g., Vatican, Duomo)."
            ),
            taboos = listOf(
                "Cutting long pasta with a knife.",
                "Touching fruit or vegetables with bare hands at open-air markets (use plastic gloves provided)."
            ),
            emergencyNumbers = "European Emergency: 112 | Police (Carabinieri): 112 | Ambulance: 118"
        ),
        CountryEtiquette(
            country = "Spain",
            flag = "🇪🇸",
            languageCode = "es",
            greetings = "¡Hola! or ¡Buenos días! Two kisses on the cheeks ('dos besos') for acquaintances.",
            tippingCustom = "Tipping is modest: 5-10% at sit-down dining if service was attentive, or small change at tapas bars.",
            diningRules = listOf(
                "Lunch ('la comida') is typically from 2:00 PM to 4:00 PM.",
                "Dinner ('la cena') starts late, usually between 9:00 PM and 11:00 PM.",
                "Tapas are meant to be shared across the entire group."
            ),
            etiquetteTips = listOf(
                "Many independent shops close for afternoon siesta (2:00 PM - 5:00 PM).",
                "Ask for the check by saying 'La cuenta, por favor'."
            ),
            taboos = listOf(
                "Showing up for dinner at 6:00 PM expecting kitchen service (only tourist traps are open).",
                "Wearing beachwear or swimwear far off the beach in city centers."
            ),
            emergencyNumbers = "European Emergency: 112 | National Police: 091 | Medical: 061"
        ),
        CountryEtiquette(
            country = "South Korea",
            flag = "🇰🇷",
            languageCode = "ko",
            greetings = "Annyeonghaseyo (안녕하세요) with a two-hand gesture or slight bow.",
            tippingCustom = "No tipping. Exceptional service is included in all bills.",
            diningRules = listOf(
                "Wait for the eldest person at the table to lift their spoon before starting to eat.",
                "When pouring or receiving drinks, use both hands to show respect to elders.",
                "Do not stick metal chopsticks upright in rice."
            ),
            etiquetteTips = listOf(
                "T-Money card works seamlessly across Seoul subways, buses, and convenience stores.",
                "Remove shoes when entering guesthouses, traditional barbecue joints, or temple rooms."
            ),
            taboos = listOf(
                "Blowing your nose at the dining table (step away to the restroom).",
                "Writing someone's name in red ink (symbolizes death or misfortune)."
            ),
            emergencyNumbers = "Police: 112 | Fire & Ambulance: 119"
        ),
        CountryEtiquette(
            country = "Thailand",
            flag = "🇹🇭",
            languageCode = "th",
            greetings = "The 'Wai' (pressing palms together at chest level with a slight bow) + 'Sawatdee khrap/kha'.",
            tippingCustom = "Small tips (20-50 THB) are warmly appreciated in sit-down restaurants and massages.",
            diningRules = listOf(
                "Fork and spoon are used: hold spoon in right hand and use fork in left hand to push food onto the spoon.",
                "Chopsticks are only used for noodle soup bowls, not for rice dishes.",
                "Dishes are served family-style to share."
            ),
            etiquetteTips = listOf(
                "Dress modestly for temples (shoulders and knees covered; take off shoes).",
                "Never touch anyone on the head, as it is considered the most sacred part of the body."
            ),
            taboos = listOf(
                "Pointing the soles of your feet at people or Buddha statues.",
                "Disrespecting or speaking critically of the Royal Family (severe legal consequences)."
            ),
            emergencyNumbers = "Tourist Police: 1155 | Emergency: 191 | Medical: 1669"
        ),
        CountryEtiquette(
            country = "Germany",
            flag = "🇩🇪",
            languageCode = "de",
            greetings = "Guten Tag! Firm handshake with direct eye contact.",
            tippingCustom = "Round up to the nearest euro or add 5-10% ('Trinkgeld'). State the total amount to pay when paying the server.",
            diningRules = listOf(
                "Say 'Guten Appetit' before eating.",
                "Always look someone straight in the eye when clinking glasses and saying 'Prost!'.",
                "Water in restaurants is almost always sparkling ('mit Kohlensäure'); specify 'stilles Wasser' for tap/still."
            ),
            etiquetteTips = listOf(
                "Punctuality is crucial for trains, tours, and appointments.",
                "Sunday is 'Ruhetag' (quiet day); most supermarkets and retail shops are strictly closed.",
                "Separate recycling strictly (Pfand bottle deposit returns at supermarkets)."
            ),
            taboos = listOf(
                "Crossing the street on a red pedestrian light (even if no cars are visible).",
                "Being loud in residential areas or apartment hallways after 10:00 PM."
            ),
            emergencyNumbers = "Emergency & Fire: 112 | Police: 110"
        ),
        CountryEtiquette(
            country = "Mexico",
            flag = "🇲🇽",
            languageCode = "es",
            greetings = "¡Buenos días! or ¡Buenas tardes! Politeness and warmth are cherished.",
            tippingCustom = "Propina of 10-15% is standard in restaurants and bars.",
            diningRules = listOf(
                "Say '¡Buen provecho!' when passing someone eating or beginning a meal.",
                "Tacos are eaten with hands; do not use a fork and knife on street tacos.",
                "Spicy salsa warning: 'No pica' often still has a kick; 'Pica poco' is quite spicy for foreigners."
            ),
            etiquetteTips = listOf(
                "Use formal 'Usted' when speaking to elders, taxi drivers, and service staff initially.",
                "Always carry smaller peso bills ($20, $50, $100) for markets and street stalls."
            ),
            taboos = listOf(
                "Drinking unbottled tap water (always drink purified/bottled water).",
                "Rejecting hospitality or a offered drink abruptly without gentle courtesy."
            ),
            emergencyNumbers = "General Emergency: 911 | Tourist Assistance: 078"
        )
    )

    fun getEtiquetteForLanguage(langCode: String): CountryEtiquette {
        return COUNTRY_ETIQUETTES.firstOrNull { it.languageCode == langCode }
            ?: COUNTRY_ETIQUETTES[0]
    }

    // Essential curated travel phrases with instant translations for offline/instant use
    fun getPhrasesForScenario(scenario: TravelScenario, targetLang: Language): List<TravelPhrase> {
        return when (scenario) {
            TravelScenario.EMERGENCY -> listOf(
                TravelPhrase(
                    id = "em_1",
                    scenario = scenario,
                    english = "I have a severe peanut allergy. Does this contain nuts or peanut oil?",
                    translation = when (targetLang.code) {
                        "ja" -> "重度の落花生（ピーナッツ）アレルギーがあります。ナッツやピーナッツ油は入っていますか？"
                        "es" -> "Tengo una alergia severa al maní/cacahuate. ¿Contiene frutos secos o aceite de maní?"
                        "fr" -> "J'ai une allergie grave aux arachides. Est-ce que cela contient des noix ou de l'huile d'arachide ?"
                        "it" -> "Ho una grave allergia alle arachidi. Contiene frutta a guscio o olio di arachidi?"
                        "de" -> "Ich habe eine schwere Erdnussallergie. Enthält dies Nüsse oder Erdnussöl?"
                        "ko" -> "땅콩 알레르기가 심합니다. 견과류나 땅콩기름이 들어가 있나요?"
                        "zh" -> "我对花生严重过敏。请问这里面含有坚果或花生油吗？"
                        "th" -> "ฉันแพ้ถั่วลิสงอย่างรุนแรง อาหารนี้มีถั่วหรือน้ำมันถั่วไหม?"
                        else -> "Emergency allergy warning: Severe peanut allergy!"
                    },
                    phonetic = when (targetLang.code) {
                        "ja" -> "Jūdo no piinattsu arerugii ga arimasu. Nattsu ya piinattsu-yu wa haitte imasu ka?"
                        "ko" -> "Ttang-kong al-le-leu-gi-ga sim-hap-ni-da. Gyeon-gwa-ryu-na ttang-kong-gi-reum-i deul-eo-ga it-na-yo?"
                        "zh" -> "Wǒ duì huāshēng yánzhòng guòmǐn. Qǐngwèn zhè lǐmiàn hán yǒu jiānguǒ huò huāshēngyóu ma?"
                        else -> ""
                    },
                    isEmergency = true,
                    dietaryAlert = true
                ),
                TravelPhrase(
                    id = "em_2",
                    scenario = scenario,
                    english = "I need immediate medical help! Please call an ambulance.",
                    translation = when (targetLang.code) {
                        "ja" -> "至急医療の助けが必要です！救急車を呼んでください。"
                        "es" -> "¡Necesito ayuda médica inmediata! Por favor llame a una ambulancia."
                        "fr" -> "J'ai besoin d'une aide médicale urgente ! Appelez une ambulance, s'il vous plaît."
                        "it" -> "Ho bisogno di aiuto medico immediato! Per favore chiamate un'ambulanza."
                        "de" -> "Ich brauche sofort medizinische Hilfe! Bitte rufen Sie einen Krankenwagen."
                        "ko" -> "당장 치료가 필요합니다! 구급차를 불러주세요."
                        "zh" -> "我需要紧急医疗救助！请帮我叫救护车。"
                        "th" -> "ฉันต้องการความช่วยเหลือทางการแพทย์ด่วน! กรุณาเรียกรถพยาบาล"
                        else -> "Medical emergency: Call an ambulance!"
                    },
                    phonetic = when (targetLang.code) {
                        "ja" -> "Shikyū iryō no tasuke ga hitsuyō desu! Kyūkyūsha o yonde kudasai."
                        "ko" -> "Dang-jang chi-ryo-ga pil-yo-hap-ni-da! Gu-geup-cha-reul bul-leo-ju-se-yo."
                        "zh" -> "Wǒ xūyào jǐnjí yīliáo jiùzhù! Qǐng bāng wǒ jiào jiùhùchē."
                        else -> ""
                    },
                    isEmergency = true
                ),
                TravelPhrase(
                    id = "em_3",
                    scenario = scenario,
                    english = "I lost my passport. Where is the nearest police station or embassy?",
                    translation = when (targetLang.code) {
                        "ja" -> "パスポートを紛失しました。一番近い交番または大使館はどこですか？"
                        "es" -> "Perdí mi pasaporte. ¿Dónde está la estación de policía o embajada más cercana?"
                        "fr" -> "J'ai perdu mon passeport. Où se trouve le commissariat ou l'ambassade la plus proche ?"
                        "it" -> "Ho perso il passaporto. Dov'è la stazione di polizia o l'ambasciata più vicina?"
                        "de" -> "Ich habe meinen Reisepass verloren. Wo ist die nächste Polizeistation oder Botschaft?"
                        "ko" -> "여권을 잃어버렸습니다. 가장 가까운 경찰서나 대사관이 어디인가요?"
                        "zh" -> "我的护照丢了。请问最近的警察局或大使馆在哪里？"
                        else -> "I lost my passport. Where is the police or embassy?"
                    },
                    phonetic = when (targetLang.code) {
                        "ja" -> "Pasupōto o funshitsu shimashita. Ichiban chikai kōban matawa taishikan wa doko desu ka?"
                        "ko" -> "Yeo-gwon-eul il-eo-beo-ryeot-seum-ni-da. Ga-jang ga-kka-un gyeong-chal-seo-na dae-sa-gwan-i eo-di-in-ga-yo?"
                        else -> ""
                    },
                    isEmergency = true
                ),
                TravelPhrase(
                    id = "em_4",
                    scenario = scenario,
                    english = "I have celiac disease (gluten allergy). Can I eat this safely?",
                    translation = when (targetLang.code) {
                        "ja" -> "セリアック病（グルテンアレルギー）です。これは小麦や醤油なしで安全に食べられますか？"
                        "es" -> "Tengo enfermedad celíaca (alergia al gluten). ¿Es seguro para mí comer esto?"
                        "fr" -> "Je suis cœliaque (allergie au gluten). Puis-je manger cela sans danger ?"
                        "it" -> "Sono celiaco (allergico al glutine). Posso mangiare questo in sicurezza?"
                        "de" -> "Ich habe Zöliakie (Glutenallergie). Kann ich das gefahrlos essen?"
                        "ko" -> "글루텐 알레르기(셀리악병)가 있습니다. 밀가루 없이 안전하게 먹을 수 있나요?"
                        "zh" -> "我有麸质过敏（乳糜泻）。请问我可以安全食用这个吗？"
                        else -> "Gluten free query: Is this safe for celiac disease?"
                    },
                    phonetic = when (targetLang.code) {
                        "ja" -> "Seriakku-byō (guruten arerugii) desu. Kore wa komugi ya shōyu nashi de anzen ni taberaremasu ka?"
                        else -> ""
                    },
                    dietaryAlert = true
                )
            )

            TravelScenario.FOOD_DINING -> listOf(
                TravelPhrase(
                    id = "fd_1",
                    scenario = scenario,
                    english = "A table for two, please. Do you have an English menu?",
                    translation = when (targetLang.code) {
                        "ja" -> "2名です。英語のメニューはありますか？"
                        "es" -> "Una mesa para dos, por favor. ¿Tienen menú en inglés?"
                        "fr" -> "Une table pour deux, s'il vous plaît. Avez-vous un menu en anglais ?"
                        "it" -> "Un tavolo per due, per favore. Avete un menu in inglese?"
                        "de" -> "Ein Tisch für zwei, bitte. Haben Sie eine englische Speisekarte?"
                        "ko" -> "두 명 자리 있나요? 영어 메뉴판이 있습니까?"
                        "zh" -> "请给我们两位的位置。请问有英文菜单吗？"
                        else -> "Table for two, please. English menu?"
                    },
                    phonetic = when (targetLang.code) {
                        "ja" -> "Ni-mei desu. Eigo no menyū wa arimasu ka?"
                        "ko" -> "Du myeong ja-ri it-na-yo? Yeong-eo me-nyu-pan-i it-seup-ni-kka?"
                        "zh" -> "Qǐng gěi wǒmen liǎng wèi de wèizhi. Qǐngwèn yǒu yīngwén càidān ma?"
                        else -> ""
                    }
                ),
                TravelPhrase(
                    id = "fd_2",
                    scenario = scenario,
                    english = "What is the chef's recommendation or your most popular specialty?",
                    translation = when (targetLang.code) {
                        "ja" -> "一番人気のおすすめ料理は何ですか？"
                        "es" -> "¿Cuál es la recomendación del chef o la especialidad más popular?"
                        "fr" -> "Quelle est la recommandation du chef ou votre spécialité la plus populaire ?"
                        "it" -> "Qual è la specialità della casa o il piatto più consigliato?"
                        "de" -> "Was ist die Empfehlung des Küchenchefs oder Ihre beliebteste Spezialität?"
                        "ko" -> "여기서 가장 인기 있는 추천 메뉴가 무엇인가요?"
                        "zh" -> "请问主厨推荐或最受欢迎的特色菜是什么？"
                        else -> "What is the specialty of the house?"
                    },
                    phonetic = when (targetLang.code) {
                        "ja" -> "Ichiban ninki no osusume ryōri wa nan desu ka?"
                        "ko" -> "Yeo-gi-seo ga-jang in-gi it-neun chu-cheon me-nyu-ga mu-eot-in-ga-yo?"
                        else -> ""
                    }
                ),
                TravelPhrase(
                    id = "fd_3",
                    scenario = scenario,
                    english = "Could we please have the bill? Can we pay with credit card?",
                    translation = when (targetLang.code) {
                        "ja" -> "お会計をお願いします。クレジットカードは使えますか？"
                        "es" -> "¿La cuenta, por favor? ¿Podemos pagar con tarjeta de crédito?"
                        "fr" -> "L'addition, s'il vous plaît. Peut-on payer par carte bancaire ?"
                        "it" -> "Il conto, per favore. Possiamo pagare con carta di credito?"
                        "de" -> "Die Rechnung, bitte. Können wir mit Kreditkarte zahlen?"
                        "ko" -> "계산서 부탁드립니다. 신용카드 결제 되나요?"
                        "zh" -> "请买单。我们可以刷信用卡吗？"
                        else -> "The bill, please. Do you take credit card?"
                    },
                    phonetic = when (targetLang.code) {
                        "ja" -> "O-kaikei o onegai shimasu. Kurejitto kādo wa tsukaemasu ka?"
                        "ko" -> "Gye-san-seo bu-tak-deu-rim-ni-da. Sin-yong-ka-deu gyeol-je doe-na-yo?"
                        else -> ""
                    }
                ),
                TravelPhrase(
                    id = "fd_4",
                    scenario = scenario,
                    english = "I am vegetarian. Does this dish contain any meat, pork, or fish broth?",
                    translation = when (targetLang.code) {
                        "ja" -> "ベジタリアンです。この料理にお肉や豚肉、魚のだしは入っていますか？"
                        "es" -> "Soy vegetariano. ¿Este plato contiene carne, cerdo o caldo de pescado?"
                        "fr" -> "Je suis végétarien. Ce plat contient-il de la viande, du porc ou du bouillon de poisson ?"
                        "it" -> "Sono vegetariano. Questo piatto contiene carne, maiale o brodo di pesce?"
                        "de" -> "Ich bin Vegetarier. Enthält dieses Gericht Fleisch, Schweinefleisch oder Fischbrühe?"
                        "ko" -> "저는 채식주의자입니다. 이 음식에 고기나 생선 육수가 들어가나요?"
                        "zh" -> "我吃素。请问这道菜含有肉类、猪肉或鱼汤吗？"
                        else -> "Vegetarian query: Does this have meat or fish stock?"
                    },
                    phonetic = when (targetLang.code) {
                        "ja" -> "Bejitarian desu. Kono ryōri ni o-niku ya butaniku, sakana no dashi wa haitte imasu ka?"
                        else -> ""
                    },
                    dietaryAlert = true
                )
            )

            TravelScenario.TRANSIT_DIRECTIONS -> listOf(
                TravelPhrase(
                    id = "tr_1",
                    scenario = scenario,
                    english = "Could you please take me to this address? Please turn on the meter.",
                    translation = when (targetLang.code) {
                        "ja" -> "この住所までお願いします。メーターを入れてください。"
                        "es" -> "Por favor, lléveme a esta dirección. Por favor ponga el taxímetro."
                        "fr" -> "Pouvez-vous m'emmener à cette adresse ? Veuillez allumer le compteur."
                        "it" -> "Può portarmi a questo indirizzo, per favore? Attivi il tassametro."
                        "de" -> "Können Sie mich bitte zu dieser Adresse bringen? Bitte schalten Sie das Taxameter ein."
                        "ko" -> "이 주소로 가주세요. 미터기를 켜주시기 바랍니다."
                        "zh" -> "请带我去这个地址。请打表计费。"
                        else -> "Please take me to this address on the meter."
                    },
                    phonetic = when (targetLang.code) {
                        "ja" -> "Kono jūsho made onegai shimasu. Mētā o irete kudasai."
                        "ko" -> "I ju-so-ro ga-ju-se-yo. Mi-teo-gi-reul kyeo-ju-si-gi ba-ram-ni-da."
                        else -> ""
                    }
                ),
                TravelPhrase(
                    id = "tr_2",
                    scenario = scenario,
                    english = "Which platform goes towards the central airport train?",
                    translation = when (targetLang.code) {
                        "ja" -> "空港行きの電車は何番線ホームですか？"
                        "es" -> "¿Qué andén va hacia el tren del aeropuerto central?"
                        "fr" -> "Quel quai mène au train pour l'aéroport central ?"
                        "it" -> "Quale binario porta al treno per l'aeroporto?"
                        "de" -> "Von welchem Gleis fährt der Zug zum Flughafen ab?"
                        "ko" -> "공항철도는 몇 번 승강장에서 타나요?"
                        "zh" -> "请问去机场的火车在几号站台乘车？"
                        else -> "Which platform for the airport train?"
                    },
                    phonetic = when (targetLang.code) {
                        "ja" -> "Kūkō yuki no densha wa nan-bansen hōmu desu ka?"
                        "ko" -> "Gong-hang-cheol-do-neun myeot beon seung-gang-jang-e-seo ta-na-yo?"
                        else -> ""
                    }
                ),
                TravelPhrase(
                    id = "tr_3",
                    scenario = scenario,
                    english = "Excuse me, how do I purchase a transit card or subway ticket?",
                    translation = when (targetLang.code) {
                        "ja" -> "すみません、交通系ICカードや地下鉄の切符はどうやって買えますか？"
                        "es" -> "Disculpe, ¿cómo compro una tarjeta de transporte o boleto de metro?"
                        "fr" -> "Excusez-moi, comment acheter une carte de transport ou un ticket de métro ?"
                        "it" -> "Scusi, come posso comprare un biglietto della metropolitana?"
                        "de" -> "Entschuldigung, wie kaufe ich eine Fahrkarte für die U-Bahn?"
                        "ko" -> "실례합니다, 지하철 티켓이나 교통카드는 어디서 구입하나요?"
                        "zh" -> "打扰一下，请问交通卡或地铁票怎么购买？"
                        else -> "How do I buy a subway ticket or pass?"
                    },
                    phonetic = when (targetLang.code) {
                        "ja" -> "Sumimasen, kōtsūkei aishii-kādo ya chikatetsu no kippu wa dō yatte kaemasu ka?"
                        else -> ""
                    }
                )
            )

            TravelScenario.HOTEL_LODGING -> listOf(
                TravelPhrase(
                    id = "ht_1",
                    scenario = scenario,
                    english = "Hello, I have a reservation under my name. Can I check in now?",
                    translation = when (targetLang.code) {
                        "ja" -> "こんにちは、宿泊の予約をしています。チェックインできますか？"
                        "es" -> "Hola, tengo una reservación a mi nombre. ¿Puedo hacer el check-in ahora?"
                        "fr" -> "Bonjour, j'ai une réservation à mon nom. Puis-je m'enregistrer maintenant ?"
                        "it" -> "Buongiorno, ho una prenotazione a mio nome. Posso effettuare il check-in adesso?"
                        "de" -> "Guten Tag, ich habe eine Reservierung auf meinen Namen. Kann ich einchecken?"
                        "ko" -> "안녕하세요, 예약자입니다. 지금 체크인할 수 있을까요?"
                        "zh" -> "您好，我有预订。现在可以办理入住手续吗？"
                        else -> "Check in under my reservation, please."
                    },
                    phonetic = when (targetLang.code) {
                        "ja" -> "Konnichiwa, shukuhaku no yoyaku o shite imasu. Chekkuin dekimasu ka?"
                        "ko" -> "An-nyeong-ha-se-yo, ye-yak-ja-im-ni-da. Ji-geum che-keu-in hal su it-eul-kka-yo?"
                        else -> ""
                    }
                ),
                TravelPhrase(
                    id = "ht_2",
                    scenario = scenario,
                    english = "Could we leave our luggage here until our room or flight is ready?",
                    translation = when (targetLang.code) {
                        "ja" -> "部屋の準備やフライトの時間まで、荷物を預かってもらえますか？"
                        "es" -> "¿Podríamos dejar nuestro equipaje aquí hasta que la habitación o vuelo esté listo?"
                        "fr" -> "Pourrions-nous laisser nos bagages ici en attendant notre chambre ou notre vol ?"
                        "it" -> "Possiamo lasciare i nostri bagagli qui fino al check-in o al volo?"
                        "de" -> "Können wir unser Gepäck hier abstellen, bis unser Zimmer fertig ist?"
                        "ko" -> "체크인 전이나 비행기 시간까지 짐을 맡겨둘 수 있나요?"
                        "zh" -> "在房间准备好之前，我们可以把行李寄存在这里吗？"
                        else -> "Could we store our luggage here for a few hours?"
                    },
                    phonetic = when (targetLang.code) {
                        "ja" -> "Heya no junbi ya furaito no jikan made, nimotsu o azukatte moraemasu ka?"
                        "ko" -> "Che-keu-in jeon-i-na bi-haeng-gi si-gan-kka-ji jim-eul mat-gyeo-dul su it-na-yo?"
                        else -> ""
                    }
                ),
                TravelPhrase(
                    id = "ht_3",
                    scenario = scenario,
                    english = "What is the Wi-Fi network and password, and what time is breakfast?",
                    translation = when (targetLang.code) {
                        "ja" -> "Wi-Fiのパスワードと、朝食の時間を教えていただけますか？"
                        "es" -> "¿Cuál es la contraseña del Wi-Fi y a qué hora es el desayuno?"
                        "fr" -> "Quel est le mot de passe du Wi-Fi et à quelle heure est le petit-déjeuner ?"
                        "it" -> "Qual è la password del Wi-Fi e a che ora viene servita la colazione?"
                        "de" -> "Wie lautet das WLAN-Passwort und wann gibt es Frühstück?"
                        "ko" -> "와이파이 비밀번호와 조식 시간을 알려주실 수 있나요?"
                        "zh" -> "请问Wi-Fi密码是多少，早餐几点开始供应？"
                        else -> "Wi-Fi password and breakfast hours, please?"
                    },
                    phonetic = when (targetLang.code) {
                        "ja" -> "Waifai no pasuwādo to, chōshoku no jikan o oshiete itadakemasu ka?"
                        else -> ""
                    }
                )
            )

            TravelScenario.SHOPPING -> listOf(
                TravelPhrase(
                    id = "sh_1",
                    scenario = scenario,
                    english = "How much is this? Do you offer Tax-Free shopping for international tourists?",
                    translation = when (targetLang.code) {
                        "ja" -> "これはいくらですか？外国人観光客向けの免税（タックスフリー）はできますか？"
                        "es" -> "¿Cuánto cuesta esto? ¿Ofrecen compras libres de impuestos (Tax-Free) para turistas?"
                        "fr" -> "Combien cela coûte-t-il ? Proposez-vous la détaxe (Tax-Free) pour les touristes ?"
                        "it" -> "Quanto costa questo? Offrite il servizio Tax-Free per i turisti?"
                        "de" -> "Wie viel kostet das? Bieten Sie steuerfreies Einkaufen (Tax-Free) an?"
                        "ko" -> "이것은 얼마인가요? 외국인 관광객 면세 혜택이 가능한가요?"
                        "zh" -> "请问这个多少钱？请问这里可以为国际游客办理退税（免税）吗？"
                        else -> "How much is this? Is Tax-Free available?"
                    },
                    phonetic = when (targetLang.code) {
                        "ja" -> "Kore wa ikura desu ka? Gaikokujin kankōkyaku muke no menzei wa dekimasu ka?"
                        "ko" -> "I-geot-eun eol-ma-in-ga-yo? Wae-guk-in gwan-gwang-gaek myeon-se hye-taek-i ga-neung-han-ga-yo?"
                        else -> ""
                    }
                ),
                TravelPhrase(
                    id = "sh_2",
                    scenario = scenario,
                    english = "Could I try this on? Do you have this in a larger or smaller size?",
                    translation = when (targetLang.code) {
                        "ja" -> "試着してもいいですか？これの大きいサイズ（または小さいサイズ）はありますか？"
                        "es" -> "¿Me puedo probar esto? ¿Tiene esto en una talla más grande o más pequeña?"
                        "fr" -> "Puis-je essayer ceci ? Avez-vous une taille au-dessus ou en dessous ?"
                        "it" -> "Posso provarlo? Ce l'avete in una taglia più grande o più piccola?"
                        "de" -> "Kann ich das anprobieren? Haben Sie das in einer größeren oder kleineren Größe?"
                        "ko" -> "입어봐도 될까요? 더 큰 사이즈(또는 작은 사이즈)가 있나요?"
                        "zh" -> "我可以试穿一下吗？请问有大一号或者小一号的尺码吗？"
                        else -> "Can I try this on? Other sizes available?"
                    },
                    phonetic = when (targetLang.code) {
                        "ja" -> "Shichaku shitemo ii desu ka? Kore no ōkii saizu wa arimasu ka?"
                        else -> ""
                    }
                )
            )

            TravelScenario.GENERAL -> listOf(
                TravelPhrase(
                    id = "gen_1",
                    scenario = scenario,
                    english = "Hello! Excuse me, could you please take a photo of us?",
                    translation = when (targetLang.code) {
                        "ja" -> "こんにちは！すみません、私たちの写真を撮っていただけますか？"
                        "es" -> "¡Hola! Disculpe, ¿podría tomarnos una foto, por favor?"
                        "fr" -> "Bonjour ! Excusez-moi, pourriez-vous nous prendre en photo s'il vous plaît ?"
                        "it" -> "Buongiorno! Scusi, potrebbe farci una foto per favore?"
                        "de" -> "Hallo! Entschuldigung, könnten Sie bitte ein Foto von uns machen?"
                        "ko" -> "안녕하세요! 실례지만 저희 사진 한 장만 찍어주실 수 있나요?"
                        "zh" -> "您好！打扰一下，请问可以帮我们拍张照片吗？"
                        else -> "Could you please take a photo of us?"
                    },
                    phonetic = when (targetLang.code) {
                        "ja" -> "Konnichiwa! Sumimasen, watashitachi no shashin o totte itadakemasu ka?"
                        "ko" -> "An-nyeong-ha-se-yo! Sil-rye-ji-man jeo-hui sa-jin han jang-man jji-geo-ju-sil su it-na-yo?"
                        else -> ""
                    }
                ),
                TravelPhrase(
                    id = "gen_2",
                    scenario = scenario,
                    english = "Thank you very much for your great kindness and help!",
                    translation = when (targetLang.code) {
                        "ja" -> "親切にしていただき、本当にありがとうございます！"
                        "es" -> "¡Muchas gracias por su gran amabilidad y ayuda!"
                        "fr" -> "Merci beaucoup pour votre grande gentillesse et votre aide !"
                        "it" -> "Grazie mille per la sua gentilezza e il suo aiuto!"
                        "de" -> "Vielen Dank für Ihre Freundlichkeit und Hilfe!"
                        "ko" -> "친절하게 도와주셔서 정말 감사합니다!"
                        "zh" -> "非常感谢您的热情帮助！"
                        else -> "Thank you very much for your kindness!"
                    },
                    phonetic = when (targetLang.code) {
                        "ja" -> "Shinsetsu ni shite itadaki, hontō ni arigatō gozaimasu!"
                        "ko" -> "Chin-jeol-ha-ge do-wa-ju-syeo-seo jeong-mal gam-sa-hap-ni-da!"
                        else -> ""
                    }
                ),
                TravelPhrase(
                    id = "gen_3",
                    scenario = scenario,
                    english = "Where is the nearest restroom or washroom?",
                    translation = when (targetLang.code) {
                        "ja" -> "一番近いお手洗い（トイレ）はどこですか？"
                        "es" -> "¿Dónde está el baño más cercano?"
                        "fr" -> "Où sont les toilettes les plus proches ?"
                        "it" -> "Dov'è il bagno più vicino?"
                        "de" -> "Wo ist die nächste Toilette?"
                        "ko" -> "가장 가까운 화장실이 어디에 있나요?"
                        "zh" -> "请问最近的洗手间在哪里？"
                        else -> "Where is the nearest restroom?"
                    },
                    phonetic = when (targetLang.code) {
                        "ja" -> "Ichiban chikai otearai (toire) wa doko desu ka?"
                        "ko" -> "Ga-jang ga-kka-un hwa-jang-sil-i eo-di-e it-na-yo?"
                        else -> ""
                    }
                )
            )
        }
    }
}
