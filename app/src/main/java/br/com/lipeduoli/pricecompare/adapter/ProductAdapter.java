package br.com.lipeduoli.pricecompare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

import br.com.lipeduoli.pricecompare.R;
import br.com.lipeduoli.pricecompare.model.Product;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> implements Serializable {

    private Context mContext;
    private List<Product> mProductList;
    private Product mProdutoMaisBarato;

    public ProductAdapter(Context context, List<Product> products) {
        this.mContext = context;
        this.mProductList = products;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_product, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = mProductList.get(position);

        if (!product.getNome().isEmpty()){
            holder.mProduto.setText(product.getNome());
        } else {
            String nomeProduto = "Produto " + (position + 1);
            holder.mProduto.setText(nomeProduto);
        }

        holder.mPeso.setText(String.format(mContext.getString(R.string.list_peso), Integer.toString(product.getPeso()), product.getTipo()));
        holder.mPreco.setText(String.format(mContext.getString(R.string.list_preco), bigDecimalToCurrencyString(product.getPreco())));

        holder.mValorProduto.setText(String.format(mContext.getString(R.string.list_valor_produto), product.getTipoConvertido(), bigDecimalToCurrencyString(product.getValorPorPeso())));

        if (product.isMenorValor()){
            holder.mMaisBarato.setVisibility(View.VISIBLE);
        } else {
            holder.mMaisBarato.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }


    public void addItem(Product product) {
        Product produtoVerificado = verificaMenorValorProduto(product);
        mProductList.add(produtoVerificado);
        notifyDataSetChanged();
    }

    private String bigDecimalToCurrencyString(BigDecimal value) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(4);
        String s = format.format(value);
        s = s.substring(0,2) + " " + s.substring(2, s.length());
        return s;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.list_product_textview_produto)
        TextView mProduto;
        @BindView(R.id.list_product_textview_peso)
        TextView mPeso;
        @BindView(R.id.list_product_textview_preco)
        TextView mPreco;
        @BindView(R.id.list_produto_textview_valor_produto)
        TextView mValorProduto;
        @BindView(R.id.list_product_imageview_mais_barato)
        ImageView mMaisBarato;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    private Product verificaMenorValorProduto(Product product) {
        if (mProdutoMaisBarato == null){
            product.setMenorValor(true);
            mProdutoMaisBarato = product;
        } else {
            if (product.getValorPorPeso().compareTo(mProdutoMaisBarato.getValorPorPeso()) == -1){
                mProductList.get(mProductList.indexOf(mProdutoMaisBarato)).setMenorValor(false);
                product.setMenorValor(true);
                mProdutoMaisBarato = product;
                return product;
            }
        }
        return product;
    }

    public void deleteProduct(int position) {
        mProductList.remove(position);
        recalculaProdutoMaisBarato();
        notifyDataSetChanged();
    }

    private void recalculaProdutoMaisBarato() {
        mProdutoMaisBarato = null;
        for (Product p : mProductList) {
            if (mProdutoMaisBarato == null){
                p.setMenorValor(true);
                mProdutoMaisBarato = p;
            } else {
                if (p.getValorPorPeso().compareTo(mProdutoMaisBarato.getValorPorPeso()) == -1){
                    mProductList.get(mProductList.indexOf(mProdutoMaisBarato)).setMenorValor(false);
                    p.setMenorValor(true);
                    mProdutoMaisBarato = p;
                }
            }
        }
    }

    public void clearList(){
        mProductList.clear();
        notifyDataSetChanged();
        mProdutoMaisBarato = null;
    }
}
