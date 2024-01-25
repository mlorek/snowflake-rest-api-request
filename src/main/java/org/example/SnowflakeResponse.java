package org.example;

public class SnowflakeResponse {
    public ResultSetMetaData resultSetMetaData;
    public Object[] data;
    public String code;
    public String statementStatusUrl;
    public String requestId;
    public String sqlState;
    public String statementHandle;
    public String message;
    public long createdOn;
}

class ResultSetMetaData {
    public int numRows;
    public String format;
    public PartitionInfo[] partitionInfo;
    public RowType[] rowType;
}

class PartitionInfo {
    public int rowCount;
    public int uncompressedSize;
}

class RowType {
    public String name;
    public String database;
    public String schema;
    public String table;
    public long byteLength;
    public long length;
    public String type;
    public String scale;
    public String precision;
    public String nullable;
    public String collation;
}

class ErrorResponse {
    public String code;
    public String message;
    public String sqlState;
    public String statementHandle;
}
