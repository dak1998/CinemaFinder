package sightnexttestproject.cinemafinder.com.cinemafinder.Utils;

        import android.app.Activity;
        import android.content.Context;
        import android.support.annotation.LayoutRes;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;

        import java.util.ArrayList;
        import java.util.List;

        import butterknife.BindView;
        import sightnexttestproject.cinemafinder.com.cinemafinder.R;

public class CustomAdapter1 extends ArrayAdapter<String>{

    public ArrayList<String> title;
    public Activity context;
    public CustomAdapter1 (Activity context, ArrayList<String> title){
        super(context, R.layout.list1,title);
        this.context=context;
        this.title=title;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View row=inflater.inflate(R.layout.list1, null, true);

        MyTextView t1=(MyTextView) row.findViewById(R.id.list1TextView);

        t1.setText(title.get(position));

        return row;
    }
}