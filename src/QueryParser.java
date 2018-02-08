public class QueryParser {

    private static ColObj oldColumn;
    private static ColObj newColumn;

    private static void initQueryObj(String queryString, String which) {
        // Precondition 0: Generate Column Objects based on queryStrings.
        String extractedName = "";
        String extractedType = "";
        String extractedAttr = "";
        int    extractedSize = 0;
        String extractedDefaultVal = "";
        String extractedAddl;

        String section = "";
        String target  = "name";
        boolean defaultValueFound = false;

        for (String c: queryString.split("")){
            if (c.equals(" ") && target.equals("name")) {
                extractedName = section;
                section       = "";
                target        = "type";
            }
            if (c.equals("(") && target.equals("type")) {
                extractedType = section;
                section       = "";
                target        = "size";
            }
            if (c.equals(" ") && target.equals("size")) {
                extractedSize = Integer.parseInt(section);
                section       = "";
                target        = "attr";
            }
            if (c.equals(")") && target.equals("attr")) {
                extractedAttr = section;
                section       = "";
                target        = "addl";
            }
            if (c.equals("'") && target.equals("addl") && !defaultValueFound) {
                defaultValueFound = true;
            }
            if (c.equals("'") && target.equals("addl") && defaultValueFound) {
                extractedDefaultVal = section;
                section = "";
            }
            if (c.contains(" ")||c.contains(")")||c.contains("(") || c.contains("'")){/*continue...*/}
            else {
                section = section.concat(c);
            }
        }
        extractedAddl = section;

        switch(which){
            case "old":
                oldColumn = new ColObj(extractedName, extractedType, extractedAttr, extractedDefaultVal, extractedSize, extractedAddl);

            case "new":
                newColumn = new ColObj(extractedName, extractedType, extractedAttr, extractedDefaultVal, extractedSize, extractedAddl);
        }
    }


    public static void main(String[] args) {

        // TEST Script inputs...
        String queryStringOld = "SSN VARCHAR2(12 CHAR) DEFAULT 'SEATTLE'";
        initQueryObj(queryStringOld, "old");

        String queryStringNew = "SSN VARCHAR2(25 CHAR) DEFAULT 'SEATTLE'";
        initQueryObj(queryStringNew, "new");
        // TEST Script inputs ...

        /*
            EVALUATION BLOCK -- EVERYTHING BUT THE BOOLEAN VARIABLES IN THIS BLOCK
                                CAN BE REMOVED IF OUTPUT IS NOT NEEDED.
        */
        // -- Line Col Name
        Boolean namesAreSame = (
                                oldColumn.getCol_name()
                                .contentEquals(
                                newColumn.getCol_name())
        );
        System.out.println("\nColumn Name: "+((!namesAreSame)? "<< Changed":""));
        System.out.println("Old: "+oldColumn.getCol_name());
        System.out.println("New: "+newColumn.getCol_name());

        // -- Line Col Type
        Boolean typesAreSame = (
                                oldColumn.getCol_type()
                                .contentEquals(
                                newColumn.getCol_type())
        );
        System.out.println("\nColumn Type: "+((!typesAreSame)? "<< Changed":""));
        System.out.println("Old: "+oldColumn.getCol_type());
        System.out.println("New: "+newColumn.getCol_type());

        // -- Line Col Size
        Boolean sizesAreSame = (
                                String.valueOf(oldColumn.getCol_size())
                                .contentEquals(
                                String.valueOf(newColumn.getCol_size()))
        );
        System.out.println("\nColumn Size: "+((!sizesAreSame)? "<< Changed":""));
        System.out.println("Old: "+oldColumn.getCol_size());
        System.out.println("New: "+newColumn.getCol_size());

        // -- Line Col Attr
        Boolean attrsAreSame = (
                                oldColumn.getCol_attr()
                                .contentEquals(
                                newColumn.getCol_attr())
        );
        System.out.println("\nColumn Attribute: "+((!attrsAreSame)? "<< Changed":""));
        System.out.println("Old: "+oldColumn.getCol_attr());
        System.out.println("New: "+newColumn.getCol_attr());

        // -- Line Col IsIndex
        Boolean indexStateUnchanged = (
                oldColumn.isIndex()==newColumn.isIndex()
        );
        System.out.println("\nColumn Is Index?: "+((!indexStateUnchanged)? "<< Changed":""));
        System.out.println("Old: "+oldColumn.isIndex());
        System.out.println("New: "+newColumn.isIndex());

        // -- Line Col IsUnique
        Boolean uniqueStateUnchanged = (
                oldColumn.isUnique()==newColumn.isUnique()
        );
        System.out.println("\nColumn Is Unique?: "+((!uniqueStateUnchanged)? "<< Changed":""));
        System.out.println("Old: "+oldColumn.isUnique());
        System.out.println("New: "+newColumn.isUnique());

        // -- Line Col IsNullable
        Boolean nullableStateUnchanged = (
                oldColumn.isNullable()==newColumn.isNullable()
        );
        System.out.println("\nColumn Is Nullable?: "+((!nullableStateUnchanged)? "<< Changed":""));
        System.out.println("Old: "+oldColumn.isNullable());
        System.out.println("New: "+newColumn.isNullable());

        // -- Line Col IsPK
        Boolean PKStateUnchanged = (
                oldColumn.isPrimaryKey()==newColumn.isPrimaryKey()
        );
        System.out.println("\nColumn Is Primary Key?: "+((!PKStateUnchanged)? "<< Changed":""));
        System.out.println("Old: "+oldColumn.isPrimaryKey());
        System.out.println("New: "+newColumn.isPrimaryKey());

        // -- Line Col IsFK
        Boolean FKStateUnchanged = (
                oldColumn.isForeignKey()==newColumn.isForeignKey()
        );
        System.out.println("\nColumn Is Foreign Key?: "+((!FKStateUnchanged)? "<< Changed":""));
        System.out.println("Old: "+oldColumn.isForeignKey());
        System.out.println("New: "+newColumn.isForeignKey());

        // EVALUATION BLOCK
        System.out.println("\n");

    // Condition 5: Column type changed from String-Compatible type.
    // Condition 6: Column type changed to String-Compatible type.


    // Condition 1: Column properties unchanged but size increased...
        if (!sizesAreSame && (namesAreSame && typesAreSame && attrsAreSame)) {
            if (oldColumn.getCol_size() < newColumn.getCol_size()) {

                Statement alterColSize = new Statement("TESTTABLE", "alter-column", newColumn);
                String  alterStatement = alterColSize.generate();

                System.out.println("-- NEW STATEMENT: --");
                System.out.println(alterStatement);
            } else {
    // Condition 2: Column attributes unchanged, size decreased.
                System.out.println("-- WARNING: --");
                System.out.println("Column size has decreased. Cannot alter column without incurring loss/truncation of data.");
            }
        }
        if (!typesAreSame) {
            System.out.println("-- WARNING: --");
            System.out.println("Data type changed between two incompatible types (e.g. String to Integer). Cannot alter column without first migrating row data.");
        }
    // Condition 3: Column Removed.
    // Condition 4: Column added.
        if (!namesAreSame) {

        }
        if (!attrsAreSame) {

        }
        if (!indexStateUnchanged) {

        }
        if (!uniqueStateUnchanged) {

        }
        if (!nullableStateUnchanged) {

        }
        if (!PKStateUnchanged) {

        }
        if (!FKStateUnchanged) {

        }

        // Refactor to be a default case.
        if (namesAreSame
                && attrsAreSame
                && sizesAreSame
                && typesAreSame
                && indexStateUnchanged
                && uniqueStateUnchanged
                && nullableStateUnchanged
                && PKStateUnchanged
                && FKStateUnchanged
        ) {
            System.out.println("-- NO CHANGES MADE --");
        }



    }
}
