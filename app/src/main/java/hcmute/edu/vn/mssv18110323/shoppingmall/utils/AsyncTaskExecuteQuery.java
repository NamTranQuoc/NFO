package hcmute.edu.vn.mssv18110323.shoppingmall.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AsyncTaskExecuteQuery extends AsyncTask<List<Object>, Void, ResultSet> {
    private ProgressDialog mProgressDialog;
    private Context context;

    public AsyncTaskExecuteQuery(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = ProgressDialog.show(context, "Thông báo", "Vui lòng đợi", false, false);
    }

    @Override
    protected void onPostExecute(ResultSet resultSet) {
        super.onPostExecute(resultSet);
        mProgressDialog.dismiss();
    }

    @Override
    protected ResultSet doInBackground(List<Object>... input) {
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement prepStmt = null;
        ResultSet resultSet = null;

        if (connection == null) {
            return null;
        }
        try {
            prepStmt = connection.prepareStatement(input[0].get(0).toString());
            input[0].remove(0);
            if (input[0] != null) {
                DatabaseUtils.setParameters(prepStmt, input[0]);
            }
            resultSet = prepStmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
}
