package cat.exemple.catfilms.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;
import cat.exemple.catfilms.model.Film;

/**
 * Created by jordi on 24/01/17.
 */

public class ListFilmAdapter extends RealmBaseAdapter<Film> implements ListAdapter {

    public ListFilmAdapter(@Nullable OrderedRealmCollection<Film> data) {
        super(data);

    }

    private static class ViewHolder {
        TextView txvTitol;
        TextView txvVersio;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txvTitol = (TextView) convertView.findViewById(android.R.id.text1);
            viewHolder.txvVersio = (TextView) convertView.findViewById(android.R.id.text2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Film item = adapterData.get(position);
        viewHolder.txvTitol.setText(item.getTitol());
        viewHolder.txvVersio.setText(item.getVersio());

        return convertView;
    }
}
