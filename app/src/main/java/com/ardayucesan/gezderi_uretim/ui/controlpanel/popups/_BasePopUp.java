package com.ardayucesan.gezderi_uretim.ui.controlpanel.popups;

import com.ardayucesan.gezderi_uretim.ui.controlpanel.adapters.FaultAdapter;
import com.ardayucesan.gezderi_uretim.ui.controlpanel.adapters.TicketAdapter;

public interface _BasePopUp {

    interface FaultPopUp {
        void showPopUp(FaultAdapter faultAdapter,Boolean isForced);

        void initListenerComponents(FaultAdapter faultAdapter);

        void hidePopUp();

    }

    interface TicketPopUp{
        void showPopUp(TicketAdapter ticketAdapter);

        void initListenerComponents(TicketAdapter ticketAdapter);

        void hidePopUp();

    }
//        interface TicketPopUps{
//            void showPopUp(Context context);
//
//            void initListenerComponents();
//
//            void hidePopUp();
//        }


}
