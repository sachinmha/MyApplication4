package image.com.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by DoublePakka on 19-Feb-18.
 */

public class WorkHistoryAdapter extends RecyclerView.Adapter<WorkHistoryAdapter.MyViewHolder> {
    Date date;
    private List<CMDSpeaksModel> list;
    Context mContext;


    public WorkHistoryAdapter(Context mContext, List<CMDSpeaksModel> list) {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_card_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        CMDSpeaksModel completeModel = list.get(position);


        String time = completeModel.getCon_title();
        String name = completeModel.getCon_id();

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_time, tv_service_name, tv_service_provider, tv_address;

        MyViewHolder(View itemView) {
            super(itemView);

            tv_time = (TextView) itemView.findViewById(R.id.tv_message);
          /*  tv_service_name = (TextView) itemView.findViewById(R.id.tv_service_name);
            tv_service_provider = (TextView) itemView.findViewById(R.id.tv_service_provider);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
*/
        }

        @Override
        public void onClick(View v) {

        }
    }


    private String capitalWord(String str) {

        // Create a char array of given String
        char ch[] = str.toCharArray();
        for (int i = 0; i < str.length(); i++) {

            // If first character of a word is found
            if (i == 0 && ch[i] != ' ' ||
                    ch[i] != ' ' && ch[i - 1] == ' ') {

                // If it is in lower-case
                if (ch[i] >= 'a' && ch[i] <= 'z') {

                    // Convert into Upper-case
                    ch[i] = (char) (ch[i] - 'a' + 'A');
                }
            }

            // If apart from first character
            // Any one is in Upper-case
            else if (ch[i] >= 'A' && ch[i] <= 'Z')

                // Convert into Lower-Case
                ch[i] = (char) (ch[i] + 'a' - 'A');
        }

        // Convert the char array to equivalent String
        return new String(ch);
    }
}
