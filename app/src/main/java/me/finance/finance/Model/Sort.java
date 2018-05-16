package me.finance.finance.Model;

public class Sort {
    private Column column;
    private Order order;

    public Sort(Column column, Order order) {
        this.column = column;
        this.order = order;
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Sort))
            return false;
        Sort element = (Sort) obj;
        if(!column.equals(element.column) || !order.equals(element.order))
            return false;
        return true;
    }

    public enum Order {
        ASC("ascending","asc"),
        DESC("descending","desc");

        private String order;
        private String databasename;

        Order(String order, String databasename){
            this.order = order;
            this.databasename = databasename;
        }

        public String getDatabasename() {
            return databasename;
        }

        @Override
        public String toString() {
            return order;
        }
    }

    public enum Column {
        DATE("Date","date"),
        VALUE("Value","value"),
        NAME("Name","name"),
        CATEGORIE("Category","category"),
        PAYMENT_OPTIONS("Paymentoptions","payment_opt");

        private String column;
        private String databasename;

        Column(String column, String databasename){
            this.column = column;
            this.databasename = databasename;
        }

        public String getDatabasename() {
            return databasename;
        }

        @Override
        public String toString() {
            return column;
        }
    }

    @Override
    public String toString() {
        return column + " " + order;
    }
}
