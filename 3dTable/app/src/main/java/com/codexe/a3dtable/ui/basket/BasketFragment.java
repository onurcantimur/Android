package com.codexe.a3dtable.ui.basket;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codexe.a3dtable.Adapters.BasketAdapter;
import com.codexe.a3dtable.R;
import com.codexe.a3dtable.model.Product;
import com.codexe.a3dtable.ui.products.SelectedProductVM;

import java.util.ArrayList;

public class BasketFragment extends Fragment implements BasketAdapter.OnBasketListener { //Adapter sinifa gonderebilmek icin arayuzu implemente ediyoruz

    private BasketModelView mViewModel;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private NavController navController;
    private ArrayList<Product> products;
    private BasketAdapter adapter;
    private TextView txt_price;
    private long total_price = 0;
    private Button btn_complete;
    private RelativeLayout pnl_bottom;


    private MutableLiveData<ArrayList<Product>> mutableLiveData;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }


    private void init(View root) {

        mViewModel = new ViewModelProvider(requireActivity()).get(BasketModelView.class);

        mutableLiveData = mViewModel.getProducts(); //Sepetteki urunleri aldigimiz sinif

        products = mutableLiveData.getValue();

        recyclerView = root.findViewById(R.id.fr_basket_rv);

        layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

        adapter = new BasketAdapter(getContext(), products, BasketFragment.this); //adapter'tan gelen pozisyon bilgisini kullanmak icin kendi referansimizi veriyoruz

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        txt_price = root.findViewById(R.id.fr_basket_txt_price);
        btn_complete = root.findViewById(R.id.fr_basket_btn_basket_confirm);
        pnl_bottom = root.findViewById(R.id.fr_basket_bottom_pnl);

        mutableLiveData.observe(requireActivity(), new Observer<ArrayList<Product>>() { //Sepetteki veride bir degisiklik olursa ui guncelliyoruz
            @Override
            public void onChanged(ArrayList<Product> newList) {
                adapter.updateList(newList); //adaptordeki verieri guncelle
                products = newList; // yeni listeyi degistir
                total_price = 0; // toplam ucreti sifirla
                products.forEach(products -> total_price += products.getPrice()); // yeni ucretleri tekrar hesapla

                if(total_price != 0)
                    pnl_bottom.setVisibility(View.VISIBLE);
                else
                    pnl_bottom.setVisibility(View.INVISIBLE);


                txt_price.setText(String.format("Toplam Tutar: %d ₺", total_price)); // toplam ucreti yazdir

            }
        });

        btnCompleteClick();

    }

    private void btnCompleteClick(){
        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BasketFragmentDirections.ActionNavBasketFragmentToNavOrder action = BasketFragmentDirections.actionNavBasketFragmentToNavOrder(total_price);
                navController.navigate(action);
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_basket, container, false);
        init(root);



        return root;

    }

    @Override
    public void basketOnclick(int position, int id) {  // tiklama eventi ile yakaklanan index bilgisini veren metot

        Product product = products.get(position);


        if (id == R.id.fr_basket_btn_delete) { // Eğer sil butonuna tiklanmis ise
            mViewModel.removeProduct(product);
        }
        else { // yada detayı icin urun'e tiklanmis ise

            SelectedProductVM selectedProductVM = new ViewModelProvider(requireActivity()).get(SelectedProductVM.class); //viewmodeli instace ediyoruz

            selectedProductVM.setProduct(product); // secilen urun bilgisini tiklanmis gibi gosteriyoruz

            navController.navigate(R.id.action_nav_basketFragment_to_nav_productDetail); // secilen urunle birlikte goruntulemesini sagliyoruz
        }
    }

}
