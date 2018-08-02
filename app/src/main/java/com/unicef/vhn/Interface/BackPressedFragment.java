package com.unicef.vhn.Interface;

public interface BackPressedFragment {
    // Note for this to work, name AND tag must be set anytime the fragment is added to back stack, e.g.
    // getActivity().getSupportFragmentManager().beginTransaction()
    //                .replace(R.id.fragment_container, MyFragment.newInstance(), "MY_FRAG_TAG")
    //                .addToBackStack("MY_FRAG_TAG")
    //                .commit();
    // This is really an override. Should call popBackStack itself.
    void onPopBackStack();
}
