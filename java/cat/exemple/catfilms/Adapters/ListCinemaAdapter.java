package cat.exemple.catfilms.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import cat.exemple.catfilms.model.Cinema;
import cat.exemple.catfilms.model.Film;
import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

/**
 * Created by jordi on 24/01/17.
 */

public class ListCinemaAdapter extends RealmBaseAdapter<Cinema> implements ListAdapter {

    public ListCinemaAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Cinema> data) {
        super(context, data);

    }

    private static class ViewHolder {
        TextView txvNom;
    }

   @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txvNom = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Cinema item = adapterData.get(position);
        viewHolder.txvNom.setText(item.getNom());

        return convertView;
    }
}
