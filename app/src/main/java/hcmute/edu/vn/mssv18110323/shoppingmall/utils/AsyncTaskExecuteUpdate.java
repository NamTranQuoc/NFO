package hcmute.edu.vn.mssv18110323.shoppingmall.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class AsyncTaskExecuteUpdate extends AsyncTask<List<Object>, Void, Integer> {
    ProgressDialog mProgressDialog;
    Context context;

    public AsyncTaskExecuteUpdate(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = ProgressDialog.show(context, "Thông báo", "Vui lòng đợi", false, false);
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        mProgressDialog.dismiss();
    }

    @Override
    protected Integer doInBackground(List<Object>... input) {
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement prepStmt = null;
        Integer result = 0;

        if (connection == null) {
            return null;
        }
        try {
            prepStmt = connection.prepareStatement(input[0].get(0).toString());
            input[0].remove(0);
            if (input[0] != null) {
                DatabaseUtils.setParameters(prepStmt, input[0]);
            }
            result = prepStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (prepStmt != null) {
                try {
                    prepStmt.close();
                } catch (SQLException ex) {
                    // ignore
                }
            }
        }
        return result;
    }
}
