package com.techstudio.erp.moneychanger.server.gae;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.techstudio.erp.moneychanger.client.util.DataResetService;
import com.techstudio.erp.moneychanger.server.domain.*;
import com.techstudio.erp.moneychanger.server.service.*;
import com.techstudio.erp.moneychanger.shared.domain.Address;

import java.io.*;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author Nilson
 */
public class DataReset
    extends RemoteServiceServlet implements DataResetService {

  private static final DateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");

  private Map<String, Category> categoryMap;
  private Map<String, Currency> currencyMap;
  private Map<String, Country> countryMap;
  private Map<String, Uom> uomMap;
  private Map<String, IndividualClient> clientMap;
  private Map<String, Item> itemMap;
  private Map<String, String> itemImagesMap;

  private boolean categorySetupCompleted = false;
  private boolean currencySetupCompleted = false;
  private boolean uomSetupCompleted = false;
  private boolean clientSetupCompleted = false;

  @Override
  public String resetData() {
    resetCurrencies();
    resetUoms();

    return null;
  }

  private void onCategorySetupCompleted() {
    categorySetupCompleted = true;
    onDomainDataSetupCompleted();
  }

  private void onCurrencySetupCompleted() {
    currencySetupCompleted = true;
    resetCountries();
    onDomainDataSetupCompleted();
  }

  private void onUomSetupCompleted() {
    uomSetupCompleted = true;
    resetCategories();
    onDomainDataSetupCompleted();
  }

  private void onIndividualClientSetupCompleted() {
    resetCompanyClients();
  }

  private void onCompanyClientSetupCompleted() {
    onClientSetupCompleted();
  }

  private void onClientSetupCompleted() {
    clientSetupCompleted = true;
    onDomainDataSetupCompleted();
  }

  private void onDomainDataSetupCompleted() {
    if (categorySetupCompleted
        && currencySetupCompleted
        && uomSetupCompleted
        && clientSetupCompleted) {
      resetItems();
    }
  }

  private void onCountrySetupCompleted() {
    resetClients();
  }

  private void onItemSetupCompleted() {
    resetSpotRates();
  }

  private void onSpotRateSetupCompleted() {
    System.out.println("Test data setup completed");
  }

  private void resetCategories() {
    List<Category> categories = Lists.newArrayList();
    for (ArrayList<String> category : readTextFromResource("category.txt")) {
      assert category.size() == 3 : "Category not of size 3: " + category;
      Category myCategory = new Category();
      myCategory.setCode(category.get(0));
      myCategory.setName(category.get(1));
      myCategory.setUom(uomMap.get(category.get(2)));
      categories.add(myCategory);
    }

    categorySetupCompleted = false;
    CategoryDao categoryDao = new CategoryDao();

    categoryDao.deleteAll(categoryDao.listAll());
    System.out.println("Categories purgeAll");

    categoryDao.putAll(categories);
    categoryMap = Maps.newHashMap();
    for (Category category : categories) {
      categoryMap.put(category.getCode(), category);
    }
    System.out.println("Categories save And map");

    System.out.println("Categories setup done");
    onCategorySetupCompleted();
  }

  private void resetCurrencies() {
    List<Currency> currencies = Lists.newArrayList();
    for (ArrayList<String> currency : readTextFromResource("currency.txt")) {
      assert currency.size() == 3 : "Currency not of size 3: " + currency;
      Currency myCurrency = new Currency();
      myCurrency.setCode(currency.get(0));
      myCurrency.setName(currency.get(1));
      myCurrency.setFullName(currency.get(2));
      currencies.add(myCurrency);
    }

    currencySetupCompleted = false;
    CurrencyDao currencyDao = new CurrencyDao();

    currencyDao.deleteAll(currencyDao.listAll());
    System.out.println("Currencies purgeAll");

    currencyDao.putAll(currencies);
    currencyMap = Maps.newHashMap();
    for (Currency currency : currencies) {
      currencyMap.put(currency.getCode(), currency);
    }
    System.out.println("Currencies save And map");

    System.out.println("Currencies setup done");
    onCurrencySetupCompleted();
  }

  private void resetUoms() {
    List<Uom> uoms = Lists.newArrayList();
    for (ArrayList<String> uom : readTextFromResource("uom.txt")) {
      assert uom.size() == 3 : "Uom not of size 3: " + uom;
      Uom myUom = new Uom();
      myUom.setCode(uom.get(0));
      myUom.setName(uom.get(1));
      myUom.setScale(Integer.parseInt(uom.get(2)));
      uoms.add(myUom);
    }

    uomSetupCompleted = false;
    UomDao uomDao = new UomDao();

    uomDao.deleteAll(uomDao.listAll());
    System.out.println("Uoms purgeAll");

    uomDao.putAll(uoms);
    uomMap = Maps.newHashMap();
    for (Uom uom : uoms) {
      uomMap.put(uom.getCode(), uom);
    }
    System.out.println("Uoms save And map");

    System.out.println("Uoms setup done");
    onUomSetupCompleted();
  }

  private void resetClients() {
    resetIndividualClients();
  }

  private void resetIndividualClients() {
    List<IndividualClient> individualClients = Lists.newArrayList();

    for (ArrayList<String> client : readTextFromResource("client-i.txt")) {
      IndividualClient individualClient = new IndividualClient();
      individualClient.setCode(client.get(0));
      individualClient.setName(client.get(1));
      individualClient.setNric(client.get(2));
      List<String> longAddress = Lists.newArrayList(Splitter.on("|").trimResults().split(client.get(3)));
      Address address = new Address();
      if (!longAddress.isEmpty() && longAddress.size() >= 3) {
        address.setLine1(longAddress.get(0));
        address.setLine2(longAddress.get(1));
        address.setPostcode(longAddress.get(2));
      }
      individualClient.setAddress(address);
      individualClient.setCountry(countryMap.get(client.get(4)));
      if (!client.get(5).isEmpty()) {
        try {
          individualClient.setDateOfBirth(dtf.parse(client.get(5)));
        } catch (ParseException e) {
          e.printStackTrace();
          individualClient.setDateOfBirth(null);
        }
      }
      individualClient.setNationality(countryMap.get(client.get(6)));
      individualClient.setContactNo(client.get(7));
      individualClient.setContactNo2(client.get(8));
      individualClient.setFaxNo(client.get(9));
      individualClient.setEmail(client.get(10));
      individualClient.setJobTitle(client.get(11));

      individualClients.add(individualClient);
    }

    IndividualClientDao individualClientDao = new IndividualClientDao();

    individualClientDao.deleteAll(individualClientDao.listAll());
    System.out.println("IndividualClients purgeAll");

    individualClientDao.putAll(individualClients);
    System.out.println("IndividualClients save And map");

    clientMap = Maps.newHashMap();
    for (IndividualClient individualClient : individualClients) {
      clientMap.put(individualClient.getCode(), individualClient);
    }

    System.out.println("IndividualClients setup done");
    onIndividualClientSetupCompleted();
  }

  private void resetCompanyClients() {
    List<CompanyClient> companyClients = Lists.newArrayList();

    for (ArrayList<String> client : readTextFromResource("client-c.txt")) {
      CompanyClient companyClient = new CompanyClient();
      companyClient.setCode(client.get(0));
      companyClient.setName(client.get(1));
      companyClient.setMoneyChanger("Y".equals(client.get(2)));
      companyClient.setRemitter("Y".equals(client.get(3)));
      companyClient.setBizRegNo(client.get(4));
      companyClient.setLicenseNo(client.get(5));
      List<String> longAddress = Lists.newArrayList(Splitter.on("|").trimResults().split(client.get(6)));
      Address address = new Address();
      if (!longAddress.isEmpty() && longAddress.size() >= 3) {
        address.setLine1(longAddress.get(0));
        address.setLine2(longAddress.get(1));
        address.setPostcode(longAddress.get(2));
      }
      companyClient.setAddress(address);
      companyClient.setCountry(countryMap.get(client.get(7)));
      companyClient.setContactNo(client.get(8));
      companyClient.setContactNo2(client.get(9));
      companyClient.setFaxNo(client.get(10));
      companyClient.setEmail(client.get(11));
      companyClient.setWebsite(client.get(12));
      List<IndividualClient> authorizedList = Lists.newArrayList();
      authorizedList.add(clientMap.get(client.get(13)));
      companyClient.setAuthorizedPersons(authorizedList);

      companyClients.add(companyClient);
    }

    CompanyClientDao companyClientDao = new CompanyClientDao();

    companyClientDao.deleteAll(companyClientDao.listAll());
    System.out.println("CompanyClients purgeAll");

    companyClientDao.putAll(companyClients);
    System.out.println("CompanyClients save And map");

    System.out.println("CompanyClients setup done");
    onCompanyClientSetupCompleted();
  }

  private void resetCountries() {
    List<Country> countries = Lists.newArrayList();
    for (ArrayList<String> country : readTextFromResource("country.txt")) {
      assert country.size() == 4 : "Country not of size 4: " + country;
      Country myCountry = new Country();
      myCountry.setCode(country.get(0));
      myCountry.setName(country.get(1));
      myCountry.setFullName(country.get(2));
      myCountry.setCurrency(currencyMap.get(country.get(3)));
      countries.add(myCountry);
    }

    CountryDao countryDao = new CountryDao();

    countryDao.deleteAll(countryDao.listAll());
    System.out.println("Countries purgeAll");

    countryDao.putAll(countries);
    System.out.println("Countries save And map");
    countryMap = Maps.newHashMap();
    for (Country country : countries) {
      countryMap.put(country.getCode(), country);
    }

    System.out.println("Countries setup done");
    onCountrySetupCompleted();
  }

  private void resetItems() {
    resetItemImages();
    List<Item> items = Lists.newArrayList();
    for (ArrayList<String> item : readTextFromResource("item.txt")) {
      assert item.size() == 5 : "Item not of size 5: " + item;
      Item myItem = new Item();
      myItem.setCode(item.get(0));
      myItem.setName(item.get(1));
      myItem.setFullName(item.get(2));
      myItem.setCategory(categoryMap.get(item.get(3)));
      myItem.setCurrency(currencyMap.get("SGD"));
      myItem.setUomRate(new BigDecimal(item.get(4)));
      String imgKey = item.get(0).toLowerCase() + ".png";
      String imgBlobKeyString = itemImagesMap.get(imgKey);
      myItem.setImageUrl(imgBlobKeyString);
      items.add(myItem);
      itemImagesMap.remove(imgKey);
    }

    ItemDao itemDao = new ItemDao();

    itemDao.deleteAll(itemDao.listAll());
    System.out.println("Items purgeAll");

    itemDao.putAll(items);
    itemMap = Maps.newHashMap();
    for (Item item : items) {
      itemMap.put(item.getCode(), item);
    }
    System.out.println("Items saveAll");

    System.out.println("Items setup done");
    onItemSetupCompleted();
  }

  private void resetItemImages() {
    itemImagesMap = Maps.newHashMap();
    InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("testdata/items.zip");

    ZipInputStream zin = new ZipInputStream(in);
    ZipEntry zipEntry;
    try {
      while ((zipEntry = zin.getNextEntry()) != null) {
        String imgName = zipEntry.getName().toLowerCase();
        String blobKeyString = getPngBlobKey(zin);
        itemImagesMap.put(imgName, blobKeyString);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("Item images setup done");
  }

  private void resetSpotRates() {
    List<Pricing> pricings = Lists.newArrayList();
    for (ArrayList<String> pricing : readTextFromResource("spotrate.txt")) {
      assert pricing.size() == 3 : "Spot Rate not of size 3: " + pricing;
      Pricing myPricing = new Pricing();
      myPricing.setCode(pricing.get(0));
      myPricing.setName(itemMap.get(pricing.get(0)).getName());
      myPricing.setBidRate(new BigDecimal(pricing.get(1)));
      myPricing.setAskRate(new BigDecimal(pricing.get(2)));
      pricings.add(myPricing);
    }

    SpotRateDao spotRateDao = new SpotRateDao();

    spotRateDao.deleteAll(spotRateDao.listAll());
    System.out.println("Spot Rates purgeAll");

    spotRateDao.putAll(pricings);
    System.out.println("Spot Rates saveAll");

    System.out.println("Spot Rates setup done");
    onSpotRateSetupCompleted();
  }

  private List<ArrayList<String>> readTextFromResource(String filename) {
    List<ArrayList<String>> list = Lists.newArrayList();

    InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("testdata/" + filename);
    if (in == null) return list;

    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

    String line;
    try {
      while ((line = reader.readLine()) != null) {
        list.add(Lists.newArrayList(Splitter.on(",").trimResults().split(line)));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return list;
  }

  /**
   * Assume that the urlString has no problems
   *
   * @param urlString read from this url
   */
  /* TODO: NILSON delete when implemented in real class
  public void readZipFromUrl(String urlString) {
    try {
      URL url = new URL(urlString);
      HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
      httpURLConnection.setRequestMethod("GET");

      ZipInputStream zin = new ZipInputStream(httpURLConnection.getInputStream());
      ZipEntry zipEntry;
      while ((zipEntry = zin.getNextEntry()) != null) {
        String zipName = zipEntry.getName().toLowerCase();
        if (zipName.endsWith("png")) {
          getPngBlobKey(zin);
        } else if (zipName.endsWith("txt")) {

        }
      }

    } catch (IOException e) {
      System.out.println("URI Exception while opening item zip file" + e.toString());
    }
  }*/

  /**
   * Stores an image file inside the blobstore and returns a String which represents the blobkey
   *
   * @param in input
   * @return String to represent the blobkey
   * @throws IOException
   */
  private String getPngBlobKey(InputStream in) throws IOException {
    FileService fileService = FileServiceFactory.getFileService();

    AppEngineFile file = fileService.createNewBlobFile("image/png");

    // Lock this because we intend to finalize
    boolean lock = true;

    byte[] blob = new byte[512];
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    BufferedInputStream inputStream = new BufferedInputStream(in);
    int bytesRead;
    while ((bytesRead = inputStream.read(blob, 0, 512)) != -1) {
      outputStream.write(blob, 0, bytesRead);
    }
    blob = outputStream.toByteArray();

    ByteBuffer buffer = ByteBuffer.wrap(blob);

    FileWriteChannel writeChannel = fileService.openWriteChannel(file, lock);
    writeChannel.write(buffer);
    writeChannel.closeFinally();

    BlobKey blobKey = fileService.getBlobKey(file);

    ImagesService imagesService = ImagesServiceFactory.getImagesService();
    // GAE developers too lazy to fix this :P
    return imagesService.getServingUrl(blobKey).replace("0.0.0.0", "127.0.0.1");
  }

}