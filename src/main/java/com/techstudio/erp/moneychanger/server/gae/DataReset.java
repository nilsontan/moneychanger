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

import java.io.*;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
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

  private Map<String, Category> categoryMap;
  private Map<String, Currency> currencyMap;
  private Map<String, Uom> uomMap;
  private Map<String, Item> itemMap;
  private Map<String, String> itemImagesMap;

  private boolean categorySetupCompleted = false;
  private boolean currencySetupCompleted = false;
  private boolean uomSetupCompleted = false;

  @Override
  public String resetData() {
    resetCategories();
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
    onDomainDataSetupCompleted();
  }

  private void onDomainDataSetupCompleted() {
    if (categorySetupCompleted
        && currencySetupCompleted
        && uomSetupCompleted) {
      resetItems();
    }
  }

  private void onCountrySetupCompleted() {
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
      assert category.size() == 2 : "Category not of size 2: " + category;
      Category myCategory = new Category();
      myCategory.setCode(category.get(0));
      myCategory.setName(category.get(1));
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
      assert uom.size() == 2 : "Uom not of size 2: " + uom;
      Uom myUom = new Uom();
      myUom.setCode(uom.get(0));
      myUom.setName(uom.get(1));
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

    System.out.println("Countries setup done");
    onCountrySetupCompleted();
  }

  private void resetItems() {
    resetItemImages();
    List<Item> items = Lists.newArrayList();
    for (ArrayList<String> item : readTextFromResource("item.txt")) {
      assert item.size() == 6 : "Item not of size 6: " + item;
      Item myItem = new Item();
      myItem.setCode(item.get(0));
      myItem.setName(item.get(1));
      myItem.setFullName(item.get(2));
      myItem.setCategory(categoryMap.get(item.get(3)));
      myItem.setCurrency(currencyMap.get("SGD"));
      myItem.setUom(uomMap.get(item.get(4)));
      myItem.setUomRate(new BigDecimal(item.get(5)));
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

    onItemSetupCompleted();
    System.out.println("Items setup done");
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
    List<SpotRate> spotRates = Lists.newArrayList();
    for (ArrayList<String> spotRate : readTextFromResource("spotrate.txt")) {
      assert spotRate.size() == 3 : "Spot Rate not of size 6: " + spotRate;
      SpotRate mySpotRate = new SpotRate();
      mySpotRate.setCode(spotRate.get(0));
      mySpotRate.setName(itemMap.get(spotRate.get(0)).getName());
      mySpotRate.setBidRate(new BigDecimal(spotRate.get(1)));
      mySpotRate.setAskRate(new BigDecimal(spotRate.get(2)));
      spotRates.add(mySpotRate);
    }

    SpotRateDao spotRateDao = new SpotRateDao();

    spotRateDao.deleteAll(spotRateDao.listAll());
    System.out.println("Spot Rates purgeAll");
    
    spotRateDao.putAll(spotRates);
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
      while((line = reader.readLine()) != null) {
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