# Transcripty
Akıllı Konuşma ve Görsel Metin Asistanı

**Transcripty**, konuşmaları gerçek zamanlı olarak metne çeviren, yazıları sesli olarak okuyabilen ve görsellerdeki metinleri tanıyan bir mobil uygulama önerisidir. Bu proje erişilebilirlik, üretkenlik ve çoklu dil desteği gibi alanlarda kullanıcıların ihtiyaçlarını karşılamayı hedeflemektedir.

---

## 📌 Problemin Tanımı

Bilgiye erişimin hızla arttığı günümüzde, kullanıcılar konuşma, yazı ve görsel içerikler arasında hızlı dönüşüm yapmakta zorluk yaşamaktadır. Transcripty, bu soruna çözüm olarak geliştirilmiş; konuşmayı metne çeviren, metni seslendiren ve görselden metin okuyabilen bir mobil asistandır.

---

## 🎯 Hedef Kitle

- Erişilebilirliğe önem veren kullanıcılar  
- Öğrenciler ve akademisyenler  
- İçerik üreticileri  
- Çok dilli ortamlarda çalışan kişiler  

---

## ⚙️ Uygulama Özellikleri

1. **Speech to Text (Konuşmayı Metne Çevirme)**  
   - Gerçek zamanlı konuşma algılama  
   - Konuşmacı ayrımı (speaker diarization)

2. **Text to Speech (Metni Seslendirme)**  
   - Farklı ses seçenekleri  
   - Hız, ton ve ses efekti ayarları

3. **Image to Speech (Görselden Metni Okuma)**  
   - Kamera veya galeriden görsel alma  
   - OCR ile görseldeki metni algılama ve sesli okuma

4. **Çoklu Dil Desteği**  
5. **Basit ve Erişilebilir Arayüz**

---

## 👤 Kullanıcı Senaryosu

Senaryo 1: Ders Kaydı ve Metinleştirme
Mert Bey, üniversitede araştırma görevlisi olarak çalışmaktadır. Bir proje toplantısı sırasında öğrencilerin fikirlerini kaçırmamak için telefonundan Transcripty uygulamasını açar.
Ana ekrandan “Sesten Metne Çevir” özelliğini seçer.
Toplantı boyunca konuşmaları gerçek zamanlı olarak algılayan uygulama, sesi metne dönüştürür.
Uygulamanın speaker diarization özelliği sayesinde, her öğrencinin konuşması ayrı ayrı tanımlanır ve metin dosyasında isimlerle eşleştirilir.
Toplantı sonunda, oluşan metin dosyası otomatik olarak Firebase Realtime Database üzerinde Mert Bey’in hesabına kaydedilir.
Mert Bey daha sonra bu dosyayı açarak hem okuyabilir hem de “Metni Seslendir” özelliğiyle yeniden dinleyebilir.

Senaryo 2: Kitaptan Görsel Alma ve Sesli Okuma
Öğrenci Elif, sınava çalışırken bir kitabın bazı bölümlerini hızlıca dinlemek ister.
Transcripty uygulamasında “Görselden Metni Okuma” özelliğini kullanarak kitabın sayfasının fotoğrafını çeker.
Uygulama bu görseldeki yazıyı OCR teknolojisi ile analiz ederek metne dönüştürür.
Elif, bu metni “Seslendir” butonuna tıklayarak doğal bir sesle dinleyebilir.
Ayrıca, uygulamanın sunduğu ses seçenekleriyle konuşma hızını ve tonunu kendine göre ayarlayabilir.

---

## 🛠️ Teknolojik Gereksinimler

- Android Studio + Kotlin + Jetpack Compose
- Google Speech-to-Text API / Whisper API (veya alternatif ücretsiz API)
- Google Text-to-Speech API / TTS API (veya alternatif ücretsiz API)
- ML Kit / Tesseract OCR (veya alternatif ücretsiz API)
- Firebase Realtime Database / Firestore (veri depolama)

---

## 🗓️ Tahmini Zaman Çizelgesi

| Hafta | Geliştirme Aşaması                             |
|-------|------------------------------------------------|
| 1     | Projenin Planlaması                            |
| 2     | Arayüz Tasarımı                                |
| 3-4   | Text to Speech özelliği                        |
| 5-6   | Speech to Text özelliği                        |
| 7-8   | Görselden Metin Okuma Entegrasyonu             |
| 9     | Dil Seçenekleri, Kullanıcı Testleri            |
| 10    | Firebase Entegrasyonu                          |
| 11-12 | Test, Geri Bildirim ve Sunuma Hazırlık         |

Projenin planlanması: Projenin genel yol haritası çizilir. Uygulamanın temel özellikleri, hedef kitlesi ve kullanılacak teknolojiler belirlenir. Ayrıca, benzer uygulamalar incelenerek gereksinim analizi yapılır. Uygulama mimarisi ve ekran akış diyagramı tasarlanır.

Arayüz tasarımı: Jetpack Compose kullanılarak uygulamanın temel kullanıcı arayüzü tasarlanır. Ekranlar arası geçişler, butonlar, metin kutuları gibi etkileşimli öğeler oluşturulur. Tasarım sade ve erişilebilir bir yapıda geliştirilir.

Text to Speech (Metni Seslendirme) Özelliği Geliştirme: Kullanıcının girdiği metni doğal seslerle seslendiren özellik geliştirilir. Google Text-to-Speech API (veya uygun bir alternatif) entegre edilir. Ses hızı, tonu ve farklı ses karakterleri gibi ayarlar yapılabilir hale getirilir.

Speech to Text (Konuşmayı Metne Çevirme) Özelliği Geliştirme: Gerçek zamanlı ses algılaması ve bu sesi metne dönüştürme özelliği eklenir. Google Speech-to-Text API, Whisper API veya uygun bir alternatif entegre edilir. Konuşmacı ayırt etme (speaker diarization) özelliği ile konuşmaların kime ait olduğu belirlenebilir hale getirilir.

Görselden Metin Okuma (OCR) Entegrasyonu: Kamera veya galeriden alınan görseller üzerinde OCR (Optik Karakter Tanıma) işlemi uygulanır. ML Kit veya Tesseract OCR gibi kütüphaneler kullanılarak görsellerdeki yazılar metne dönüştürülür. Bu metin istenirse sesli olarak da okunabilir.

Dil Seçenekleri, Ses Efektleri ve Kullanıcı Testleri: Uygulamaya çoklu dil desteği eklenir. Farklı giriş ve çıkış dilleri desteklenir. Kullanıcıya ses efekti ayarlama gibi gelişmiş seçenekler sunulur. Bu hafta aynı zamanda erken kullanıcı testleriyle geri bildirim toplanır.

Firebase Entegrasyonu ve Veri Kaydı: Uygulama ile Firebase Realtime Database veya Firestore bağlantısı kurulur. Kullanıcı verileri, kayıtlı ses dosyaları ve metinler veritabanına kaydedilir. Kullanıcı oturumları ve veri güvenliği için temel yapı kurulur.

Test ve Geri Bildirim, Hata Düzeltmeleri, Sunuma Hazırlık: Uygulamanın tüm özellikleri test edilir, kullanıcı geri bildirimleri değerlendirilerek hata düzeltmeleri yapılır. Sunum materyalleri hazırlanır, gerekli ekran görüntüleri ve demo videoları oluşturulur.

---

## 🔚 Sonuç ve Katkı

Transcripty, konuşma, metin ve görsel içerikler arasında dönüştürme gereksinimi duyan bireyler için yenilikçi ve erişilebilir bir çözüm sunmaktadır. Özellikle eğitim ve içerik üretimi gibi alanlarda çalışan kullanıcıların verimliliğini artırmayı hedefler. Uygulamanın sunduğu konuşmadan metne, metinden sese ve görselden metin/sese dönüşüm özellikleri, bilgiye erişimi kolaylaştırırken, aynı zamanda bireylerin dijital okuryazarlığını ve teknolojik adaptasyonunu güçlendirmektedir.
Sonuç olarak, Transcripty hem kullanıcı deneyimi hem de toplumsal fayda açısından değerli bir dijital ürün ortaya koymakta, erişilebilirlik, üretkenlik ve kapsayıcılık ilkelerini temel alan bir yaklaşımı benimsemektedir.

