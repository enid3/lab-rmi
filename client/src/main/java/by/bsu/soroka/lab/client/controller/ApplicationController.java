package by.bsu.soroka.lab.client.controller;

import by.bsu.soroka.lab.common.entity.Product;
import by.bsu.soroka.lab.common.entity.Stock;
import by.bsu.soroka.lab.common.entity.Storage;
import by.bsu.soroka.lab.common.service.ProductService;
import by.bsu.soroka.lab.common.service.StockService;
import by.bsu.soroka.lab.common.service.StorageService;
import by.bsu.soroka.lab.common.service.exception.ServiceException;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Optional;

/**
 * {@link ApplicationController}, used as controller for <b>client-side</b> application.
 * @author Egor Soroka
 */
@Slf4j
public class ApplicationController {
    private static final String REMOTE_ERROR = "Connection error";
    /**
     * Default host parameter for {@link java.rmi.registry.LocateRegistry#getRegistry(String, int)}
     * {@link ApplicationController#PORT }
     */
    public static final String HOST = "127.0.0.1";
    /**
     * Default port parameter for {@link java.rmi.registry.LocateRegistry#getRegistry(String, int)}
     * {@link ApplicationController#HOST}
     */
    public static final int PORT = 6443;
    private static final String PRODUCT_SERVICE_NAME = "product";
    private static final String STORAGE_SERVICE_NAME = "storage";
    private static final String STOCK_SERVICE_NAME = "stock";
    @FXML private Label selectedProductTotalCount;
    private ButtonType ADD_BUTTON_TYPE = new ButtonType("Add");

    @FXML private TabPane   tabPane;

    @FXML private TextField stockProductIDTextField;
    @FXML private TextField stockStorageIDTextField;
    @FXML private TextField stockCountTextField;

    @FXML private TableView<Product> productTabbedView;
    @FXML private TableColumn<Product, Integer> productIDColumn;
    @FXML private TableColumn<Product, String> productNameColumn;
    @FXML private TableColumn<Product, Integer> productPriceColumn;
    @FXML private TableColumn<Product, Number> countOfProductPerStorage;

    @FXML private TableView<Stock> stockTabbedView;
    @FXML private TableColumn<Stock, Integer> stockIDColumn;
    @FXML private TableColumn<Stock, Integer> stockProductIDColumn;
    @FXML private TableColumn<Stock, Integer> stockStorageIDColumn;
    @FXML private TableColumn<Stock, Integer> stockCountColumn;


    @FXML private TableView<Storage> storageTabbedView;
    @FXML private TableColumn<Storage, Integer> storageIDColumn;
    @FXML private TableColumn<Storage, String> storageNameColumn;
    @FXML private TableColumn<Storage, Number> countOfSelectedProductForStorage;
    private SimpleBooleanProperty flag = new SimpleBooleanProperty(false);

    ProductService productService;
    StorageService storageService;
    StockService stockService;

    /**
     * Generate and show <b>error-styled</b> alert window
     * @param message Message, that will be shown
     */
    public void errorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.initOwner(tabPane.getScene().getWindow());
        alert.showAndWait();
    }

    /**
     * Initialize {@link ApplicationController} data.
     * Data is received from {@link ProductService}, {@link StockService}, {@link StockService},
     * via {@link java.rmi.registry.LocateRegistry};
     * Used parameters to connect {@link ApplicationController#HOST}, {@link ApplicationController#PORT}
     */
    @FXML
    public void initialize() {
        try {
            Registry registry = LocateRegistry.getRegistry(HOST, PORT);
            productService = (ProductService) registry.lookup(PRODUCT_SERVICE_NAME);
            storageService = (StorageService)  registry.lookup(STORAGE_SERVICE_NAME);
            stockService = (StockService)  registry.lookup(STOCK_SERVICE_NAME);
        } catch (RemoteException | NotBoundException e) {
            //errorAlert(REMOTE_ERROR);
            e.printStackTrace();
            return;
        }

        initProductTableView();
        initStorageTableView();
        initStockTableView();

        try {
            updateAll(null);
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    /**
     * Reset value of {@link ApplicationController#selectedProductTotalCount} to newly gotten from {@link StockService}
     * @throws ServiceException  thrown by {@link by.bsu.soroka.lab.common.service.StockService#getTotalProductCountByID(int) getTotalProductCount}/
     * @throws RemoteException  thrown by {@link by.bsu.soroka.lab.common.service.StockService#getTotalProductCountByID(int) getTotalProductCount}/
     */
    protected void resetTotalCountLabel() throws ServiceException, RemoteException {
        Product product = productTabbedView.getSelectionModel().getSelectedItem();
        if(product != null) {
            selectedProductTotalCount.setText(
                    String.valueOf(stockService.getTotalProductCountByID(product.getId())));
        }
    }

    private void initProductTableView() {
        productIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTabbedView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                    countOfSelectedProductForStorage.setText(newSelection.getName());

            try {
                resetTotalCountLabel();
            } catch (ServiceException e) {
                errorAlert(e.getMessage());
            } catch (RemoteException e) {
                errorAlert(REMOTE_ERROR);
            }
        });

        countOfSelectedProductForStorage.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Storage, Number>, ObservableValue<Number>>() {
            public ObservableValue<Number> call(TableColumn.CellDataFeatures<Storage, Number> s) {
                IntegerBinding ib = new IntegerBinding() {
                    {
                        this.bind(productTabbedView.getSelectionModel().selectedItemProperty(), flag);
                    }
                    @Override
                    protected int computeValue() {
                        try {
                            Product product = productTabbedView.getSelectionModel().getSelectedItem();
                            if(product != null) {
                                int res = stockService.getCount(product.getId(), s.getValue().getId());
                                return res;
                            } else {
                                return 0;
                            }
                        } catch (ServiceException e) {
                            e.printStackTrace();
                            return 0;
                        } catch (RemoteException e){
                            e.printStackTrace();
                            return 0;
                        }
                }
            };
                return ib;
            }
        });

    }
    private void initStorageTableView() {
        storageIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        storageNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        storageTabbedView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                    countOfProductPerStorage.setText(newSelection.getName());
                }
        );
        countOfProductPerStorage.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Product, Number>, ObservableValue<Number>>() {
            public ObservableValue<Number> call(TableColumn.CellDataFeatures<Product, Number> p) {
                System.out.println(p.getValue());
                IntegerBinding ib = new IntegerBinding() {
                    {
                        this.bind(storageTabbedView.getSelectionModel().selectedItemProperty(), flag);
                    }
                    @Override
                    protected int computeValue() {
                        try {
                            Storage storage = storageTabbedView.getSelectionModel().getSelectedItem();
                            if(storage != null) {
                                int res = stockService.getCount(p.getValue().getId(), storage.getId());
                                return res;
                            } else {
                                return 0;
                            }
                        } catch (ServiceException e) {
                            e.printStackTrace();
                            return 0;
                        } catch (RemoteException e){
                            e.printStackTrace();
                            return 0;
                        }
                    }
                };
                return ib;
            }
        });

    }
    private void initStockTableView() {
        stockIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        stockProductIDColumn.setCellValueFactory(new PropertyValueFactory<>("productID"));
        stockStorageIDColumn.setCellValueFactory(new PropertyValueFactory<>("storageID"));
        stockCountColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
    }


    /**
     * Call {@link ApplicationController#updateProductTable(ActionEvent) updateProductTable},  {@link ApplicationController#updateStockTable(ActionEvent) updateStockTable},
     * {@link ApplicationController#updateStorageTable(ActionEvent) updateStorageTable}
     * @param actionEvent event, that cause update.
     * @throws ServiceException thrown by related service.
     * @throws RemoteException in case of some rmi error.
     */
    public void updateAll(ActionEvent actionEvent) throws ServiceException, RemoteException {
        updateProductTable(actionEvent);
        updateStockTable(actionEvent);
        updateStorageTable(actionEvent);
    }

    /**
     * Update {@link ApplicationController#productTabbedView} via {@link ApplicationController#productService}
     * @throws ServiceException thrown by related service.
     * @throws RemoteException in case of some rmi error.
     */
    public void updateProductTable(ActionEvent actionEvent) throws ServiceException, RemoteException {
        List<Product> products = productService.getAll();
        productTabbedView.getItems().clear();
        productTabbedView.getItems().addAll(products);
    }

    /**
     * Update {@link ApplicationController#stockTabbedView} via {@link ApplicationController#stockService}
     * @param actionEvent event, that cause update.
     * @throws ServiceException thrown by related service.
     * @throws RemoteException in case of some rmi error.
     */
    public void updateStockTable(ActionEvent actionEvent) throws ServiceException, RemoteException {
        stockTabbedView.getItems().clear();
        stockTabbedView.getItems().addAll(stockService.getAll());
    }

    /**
     * Update {@link ApplicationController#storageTabbedView} via {@link ApplicationController#storageService}
     * @param actionEvent event, that cause update.
     * @throws ServiceException thrown by related service.
     * @throws RemoteException in case of some rmi error.
     */
    public void updateStorageTable(ActionEvent actionEvent) throws ServiceException, RemoteException {
        storageTabbedView.getItems().clear();
        storageTabbedView.getItems().addAll(storageService.getAll());
    }


    /**
     * <b>Add</b> {@link Product} to {@link ApplicationController#productTabbedView} and to server via {@link ApplicationController#productService}.
     * @param event event, that cause update.
     */
    public void addProduct(Event event) {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("add product");

        dialog.getDialogPane().getButtonTypes().addAll(ADD_BUTTON_TYPE, ButtonType.CANCEL);
        VBox vbox = new VBox(10);
        dialog.initOwner(tabPane.getScene().getWindow());
        TextField productName = new TextField();
        productName.setPromptText("Name");
        TextField productPrice = new TextField();
        productPrice.setPromptText("Price");

        vbox.getChildren().addAll(new Label("Name:"), productName,
                new Label("Price:"), productPrice);

        dialog.getDialogPane().setContent(vbox);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ADD_BUTTON_TYPE) {
                return new Product(productName.getText(), Integer.valueOf(productPrice.getText()));
            }
            return null;
        });
        Optional<Product> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                Product product = result.get();
                product.setId(productService.add(product));
                productTabbedView.getItems().add(product);
            } catch (ServiceException e) {
                errorAlert(e.getMessage());
            }
            catch (RemoteException e){
                errorAlert(REMOTE_ERROR);
            }
        }
    }

    /**
     * <b>Add</b> {@link Storage} to {@link ApplicationController#storageTabbedView} and to server via {@link ApplicationController#storageService}.
     * @param event event, that cause update.
     */
    public void addStorage(Event event) {
        Dialog<Storage> dialog = new Dialog<>();
        dialog.setTitle("add storage");

        dialog.getDialogPane().getButtonTypes().addAll(ADD_BUTTON_TYPE, ButtonType.CANCEL);
        VBox vbox = new VBox(10);
        dialog.initOwner(tabPane.getScene().getWindow());
        TextField storageName = new TextField();
        storageName.setPromptText("Name");

        vbox.getChildren().addAll(new Label("Name:"), storageName);


        dialog.getDialogPane().setContent(vbox);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ADD_BUTTON_TYPE) {
                return new Storage(storageName.getText());
            }
            return null;
        });
        Optional<Storage> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                Storage storage = result.get();
                storage.setId(storageService.add(storage));
                storageTabbedView.getItems().add(storage);
            } catch (ServiceException e) {
                e.printStackTrace();
            }
            catch (RemoteException e){
                e.printStackTrace();
            }
    }

}

    /**
     * <b>Add</b> {@link Stock} to {@link ApplicationController#stockTabbedView} and to server via {@link ApplicationController#stockService}.
     * @param actionEvent event, that cause update.
     */
    public void onStockAdding(ActionEvent actionEvent) {
        int productID = Integer.parseInt(stockProductIDTextField.getText());
        int storageID = Integer.parseInt(stockStorageIDTextField.getText());
        int count = Integer.parseInt(stockCountTextField.getText());
        Stock stock = new Stock(productID, storageID, count);

        try {
            stock.setId(stockService.add(stock));
            resetTotalCountLabel();
        } catch (ServiceException e) {
            errorAlert(e.getMessage());
        } catch (RemoteException e) {
            errorAlert(REMOTE_ERROR);
        }
    }

    /**
     * <b>Delete</b> {@link Product} from {@link ApplicationController#productTabbedView} and server via {@link ApplicationController#productService}.
     * @param actionEvent event, that cause update.
     */
    public void onProductDelete(ActionEvent actionEvent) {
        List<Product> products = productTabbedView.getSelectionModel().getSelectedItems();
        try{
            for(Product p : products){
                productService.removeById(p.getId());
                productTabbedView.getItems().remove(p);
            }
            resetTotalCountLabel();
        } catch (ServiceException e) {
            errorAlert(e.getMessage());
        } catch (RemoteException e) {
            errorAlert(REMOTE_ERROR);
        }

    }

    /**
     * <b>Delete</b> {@link Stock} from {@link ApplicationController#stockTabbedView} and server via {@link ApplicationController#stockService}.
     * @param actionEvent event, that cause update.
     */
    public void onStockDelete(ActionEvent actionEvent) {
        List<Stock> stocks = stockTabbedView.getSelectionModel().getSelectedItems();
        try {
            for(Stock s : stocks){
                stockService.removeById(s.getId());
                stockTabbedView.getItems().remove(s);
            }
            resetTotalCountLabel();
        } catch (ServiceException e) {
            errorAlert(e.getMessage());
        } catch (RemoteException e) {
            errorAlert(REMOTE_ERROR);
        }
}

    /**
     * <b>Delete</b> {@link Storage} from {@link ApplicationController#storageTabbedView} and server via {@link ApplicationController#storageService}.
     * @param actionEvent event, that cause update.
     */
    public void onDeleteStorage(ActionEvent actionEvent) {
        List<Storage> storages = storageTabbedView.getSelectionModel().getSelectedItems();
        try {
            for(Storage s : storages){
                storageService.removeById(s.getId());
                storageTabbedView.getItems().remove(s);
            }
            resetTotalCountLabel();
        } catch (ServiceException e) {
            errorAlert(e.getMessage());
        } catch (RemoteException e) {
            errorAlert(REMOTE_ERROR);
        }
}

    /**
     * <b>Add</b> {@link Stock} to {@link ApplicationController#stockTabbedView} and to server via {@link ApplicationController#stockService}.
     * @param actionEvent event, that cause update.
     * @see {@link ApplicationController#addProduct(Event) addProduct}, {@link ApplicationController#addStorage(Event) addStorage}
     */
    public void addStock(ActionEvent actionEvent) {
        Dialog<Stock> dialog = new Dialog<>();
        dialog.setTitle("add stock");

        dialog.getDialogPane().getButtonTypes().addAll(ADD_BUTTON_TYPE, ButtonType.CANCEL);
        VBox vbox = new VBox(10);
        dialog.initOwner(tabPane.getScene().getWindow());
        TextField stockCount = new TextField();
        stockCount.setPromptText("count");
        List<Product> products =  productTabbedView.getSelectionModel().getSelectedItems();
        List<Storage> storages =  storageTabbedView.getSelectionModel().getSelectedItems();
        if(products.size() != 1 && storages.size() != 1){
            errorAlert("Only one product & storage must be selected");
            return;
        }
        Product product = products.get(0);
        Storage storage = storages.get(0);
        Label info = new Label("" + product + " to " + storage);

        vbox.getChildren().addAll(info, new Label("count:"), stockCount);


        dialog.getDialogPane().setContent(vbox);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ADD_BUTTON_TYPE) {
                return new Stock(product.getId(), storage.getId(), Integer.valueOf(stockCount.getText()));
            }
            return null;
        });
        Optional<Stock> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                Stock stock = result.get();
                stock.setId(stockService.add(stock));
                resetTotalCountLabel();
                flag.set(!flag.get());
            } catch (ServiceException e) {
                errorAlert(e.getMessage());
            } catch (RemoteException e) {
                errorAlert(REMOTE_ERROR);
            }
        }
    }

}
