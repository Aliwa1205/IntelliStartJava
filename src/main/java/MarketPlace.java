import org.omg.CosNaming.NamingContextPackage.NotFound;

import java.util.*;

public class MarketPlace {
    Customer[] customers = new Customer[3];
    Product[] products = new Product[3];
    Map<Integer, List<String>> purchases = new HashMap<Integer, List<String>>();
    Map<Integer, List<String>> productToCustomer = new HashMap<Integer, List<String>>();
    Scanner in = new Scanner(System.in);

    public void initCustomers() {
        customers[0] = new Customer(1526, "Ivan", "Budko", 10000);
        customers[1] = new Customer(2457, "Oleg", "Shylo", 3000);
        customers[2] = new Customer(3365, "Anna", "Kron", 5);
    }

    public void showCustomers(){
        System.out.println("***All Customers***");
        for (Customer el: customers) {
            System.out.println("Id: " + el.getId() + "\n" +
                               "FirstName: " + el.getFirstName() + "\n" +
                               "LastName: " + el.getLastName() + "\n" +
                               "Amount: " + el.getAmount());
        }
    }

    public void initProducts() {
        products[0] = new Product(852, "Bread", 23);
        products[1] = new Product(258, "Banana", 48);
        products[2] = new Product(825, "Potatoes", 15);
    }

    public void showProducts(){
        System.out.println("***All Products***");
        for (Product el: products) {
            System.out.println("Id: " + el.getId() + "\t" +
                               "Name: " + el.getName() + "\t" +
                               "Price: " + el.getPrice());
        }
    }

    public Product getProduct(int idProduct){
        Product oneItem = null;
        for(int i = 0; i < products.length; i++) {
            if (products[i].id == idProduct) {
                oneItem = products[i];
            }
        }

        try {
            if (oneItem == null){
                throw new NullPointerException("Exception: The Product not found");
            } else {
                System.out.println("The Product cost: " + oneItem.getPrice());
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
        return oneItem;
    }

    public Customer getCustomer(int idCustomer){
        Customer onePerson = null;
        for(int i = 0; i < customers.length; i++) {
            if (customers[i].id == idCustomer) {
                onePerson = customers[i];
            }
        }

        try {
            if (onePerson == null){
                throw new NullPointerException("Exception: The Customer does not exist");
            } else {
                System.out.println("The Customer has " + onePerson.getAmount());
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
        return onePerson;
    }

    public void decreaseBalance(int idCustomer, int amount){
        System.out.println("***Update balance***");
        for(int i = 0; i < customers.length; i++) {
            if (customers[i].id == idCustomer) {
                customers[i].amount = amount;
            }
        }
    }

    public void storePurchases(int idCustomer, int idProduct){
        System.out.println("***STORE purchases***");
        System.out.println("At the begining: " + purchases);
        int currentKey = 0;

        for (Map.Entry<Integer, List<String>> me : purchases.entrySet()) {
            if (me.getKey() == idCustomer){
                currentKey = me.getKey();
                System.out.println("currentKey = " + currentKey);
                System.out.println("i will put my purchases in existing list for " + currentKey);
                List<String> listUpdateExistPurchases = new ArrayList<String>();
                listUpdateExistPurchases = me.getValue();
                System.out.println("listUpdateExistPurchases: " + listUpdateExistPurchases);
                listUpdateExistPurchases.add(String.valueOf(idProduct));
                System.out.println("listUpdateExistPurchases + new idProduct : " + listUpdateExistPurchases);
                purchases.put(currentKey, listUpdateExistPurchases);
            }
        }

        if (currentKey == 0) {
            System.out.println("currentKey = " + currentKey);
            System.out.println("i will create new purchases list for " + idCustomer);
            purchases.put(idCustomer, new ArrayList<String>(Arrays.asList(String.valueOf(idProduct))));
        }
        System.out.println("At the end: " + purchases);

        //Attitude Product To Customer
        int currentKeyProduct = 0;
        for (Map.Entry<Integer, List<String>> me : productToCustomer.entrySet()) {
            if (me.getKey() == idProduct){
                currentKeyProduct = me.getKey();
                List<String> listUpdateExistProduct = new ArrayList<String>();
                listUpdateExistProduct = me.getValue();
                listUpdateExistProduct.add(String.valueOf(idCustomer));
                productToCustomer.put(currentKeyProduct, listUpdateExistProduct);
            }
        }
        if (currentKeyProduct == 0) {
            productToCustomer.put(idProduct, new ArrayList<String>(Arrays.asList(String.valueOf(idCustomer))));
        }
    }

    public void buyProduct(int idCustomer, int idProduct) {
        System.out.println("***Purchase***");
        Customer customer = getCustomer(idCustomer);
        Product product = getProduct(idProduct);
        int newAmount;
        try {
            if (customer.getAmount() < product.getPrice()){
                throw new UserDefinedException("Error: The Customer "+ idCustomer + ":" + customer.getFirstName() + " " + customer.getLastName() + " does not have enough money");
            } else {
                newAmount = customer.getAmount() - product.getPrice();
                decreaseBalance(customer.getId(), newAmount);
                System.out.println("Rest amount: " + newAmount);
                storePurchases(customer.getId(), product.getId());
                System.out.println("The Customer " + idCustomer + ":"+ customer.getFirstName() + " " + customer.getLastName() +" has bought the product " + product.getId() + ":" + product.getName() + " successfully!");
            }
        } catch (UserDefinedException e) {
            System.out.println("Caught the exception");
            System.out.println(e.getMessage());
        }
    }

    public int currentCustomer(){
        System.out.println("Input the idCustomer: ");
        int idCustomer = in.nextInt();
        return idCustomer;
    }

    public int currentProduct(){
        System.out.println("Input the idProduct: ");
        int idProduct = in.nextInt();
        return idProduct;
    }

    public int takeCustomerId(){
        System.out.println("Input the idCustomer to display the result of purchases: ");
        int idCustomer = in.nextInt();
        return idCustomer;
    }

    public int takeProductId(){
        System.out.println("Input the idProduct to display the result of purchases: ");
        int idProduct = in.nextInt();
        return idProduct;
    }

    public void closeScanner(){
        in.close();
    }

    public void findCustomerPurchases(int idCustomer){
        System.out.println("***Display purchases***");
        int id = 0;
        for (Map.Entry<Integer, List<String>> me : purchases.entrySet()) {
            if (me.getKey() == idCustomer){
                id = me.getKey();
                System.out.println("The CustomerId: " + me.getKey() + " has purchasesList " + me.getValue());
            }
        }

        if(id == 0) {
            System.out.println("The CustomerId: " + idCustomer + " has no purchases currently ");
        }
    }

    public void findProductPurchases(int idProduct){
        System.out.println("***Display purchases***");
        int id = 0;
        for (Map.Entry<Integer, List<String>> me : productToCustomer.entrySet()) {
            if (me.getKey() == idProduct){
                id = me.getKey();
                System.out.println("The ProductId: " + me.getKey() + " has customerList " + me.getValue());
            }
        }

        if(id == 0) {
            System.out.println("The ProductId: " + idProduct + " has no yet been purchased");
        }
    }

    public static void main(String[] args) {
        MarketPlace mp = new MarketPlace();

        mp.initCustomers();
        mp.showCustomers();
        System.out.println();

        mp.initProducts();
        mp.showProducts();
        System.out.println();

        // do shopping Customer1
        int idCustomer = mp.currentCustomer();
        int idProduct = mp.currentProduct();
        System.out.println();
        mp.buyProduct(idCustomer, idProduct);
        System.out.println();


        // do shopping Customer2
        int idCustomer2 = mp.currentCustomer();
        int idProduct2 = mp.currentProduct();
        System.out.println();
        mp.buyProduct(idCustomer2, idProduct2);
        System.out.println();

        // do shopping Customer3
        int idCustomer3 = mp.currentCustomer();
        int idProduct3 = mp.currentProduct();
        System.out.println();
        mp.buyProduct(idCustomer3, idProduct3);
        System.out.println();

        //Try1: output the result for specified customer
        int idCustomerProducts1 = mp.takeCustomerId();
        mp.findCustomerPurchases(idCustomerProducts1);
        System.out.println();

        //Try2: output the result for specified customer
        int idCustomerProducts2 = mp.takeCustomerId();
        mp.findCustomerPurchases(idCustomerProducts2);
        System.out.println();

        //Try3: output the result for specified customer
        int idCustomerProducts3 = mp.takeCustomerId();
        mp.findCustomerPurchases(idCustomerProducts3);
        System.out.println();

        //Try1: output the result for specified product
        int idSpecifiedProduct1 = mp.takeProductId();
        mp.findProductPurchases(idSpecifiedProduct1);
        System.out.println();

        //Try2: output the result for specified product
        int idSpecifiedProduct2 = mp.takeProductId();
        mp.findProductPurchases(idSpecifiedProduct2);
        System.out.println();

        //Try3: output the result for specified product
        int idSpecifiedProduct3 = mp.takeProductId();
        mp.findProductPurchases(idSpecifiedProduct3);
        System.out.println();

        //Show all purchases by customer
        List<Map.Entry<Integer, List<String>>> purchasesList = new ArrayList<>(mp.purchases.entrySet());
        System.out.println("purchasesList: " + purchasesList);
        System.out.println();

        //Show all customer who bought certain product
        List<Map.Entry<Integer, List<String>>> productList = new ArrayList<>(mp.productToCustomer.entrySet());
        System.out.println("productList: " + productList);
        System.out.println();


        // Finish/Result
        mp.closeScanner();
        mp.showCustomers();
    }

    static class Product{
        private int id;
        private String name;
        private int price;

        public Product (int id, String name, int price){
            this.id = id;
            this.name = name;
            this.price = price;
        }

        public int getId(){
            return id;
        }

        public String getName(){
            return name;
        }

        public int getPrice(){
            return price;
        }
    }

    static class Customer{
        private int id;
        private String firstName;
        private String lastName;
        private int amount;

        public Customer (int id, String firstName, String lastName, int amount){
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.amount = amount;
        }

        public int getId(){
            return id;
        }

        public String getFirstName(){
            return firstName;
        }

        public String getLastName(){
            return lastName;
        }

        public int getAmount(){
            return amount;
        }
    }
}
