package ca.ubc.cs304.delegates;

public interface ManipulateDatabaseDelegate {
    void tableList();

    void viewTable(String tableName);

    void insertIntoTable(String tableName, String values);

    void deleteFromTable(String tableName, String conditions);
}
