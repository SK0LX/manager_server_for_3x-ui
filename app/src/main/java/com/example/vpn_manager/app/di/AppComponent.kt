package com.example.vpn_manager.app.di

import android.content.Context
import com.example.vpn_manager.MainActivity
import com.example.vpn_manager.clients_screen.di.ClientsModule
import com.example.vpn_manager.clients_screen.presentation.view.ClientsFragment
import com.example.vpn_manager.inbounds_screen.di.InboundsModule
import com.example.vpn_manager.inbounds_screen.presentation.view.InboundsFragment
import com.example.vpn_manager.main_screen.di.MainModule
import com.example.vpn_manager.main_screen.presentation.view.MainFragment
import com.example.vpn_manager.metrics_screen.di.MetricsModule
import com.example.vpn_manager.metrics_screen.presentation.view.MetricsFragment
import com.example.vpn_manager.modal_client.di.AddEditClientModule
import com.example.vpn_manager.modal_client.presentation.view.AddEditClientFragment
import com.example.vpn_manager.modal_inbound.di.AddEditInboundModule
import com.example.vpn_manager.modal_inbound.domain.repository.AddEditInboundRepository
import com.example.vpn_manager.modal_inbound.presentation.view.AddEditInboundFragment
import com.example.vpn_manager.modal_server.di.AddEditServerModule
import com.example.vpn_manager.modal_server.presentation.view.AddEditServerFragment
import com.example.vpn_manager.network.NetworkProvidesModule
import com.example.vpn_manager.settings_screen.di.SettingsModal
import com.example.vpn_manager.settings_screen.presentation.view.SettingsFragment
import com.example.vpn_manager.storage.di.StorageModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    MainModule::class,
    InboundsModule::class,
    ClientsModule::class,
    MetricsModule::class,
    SettingsModal::class,
    AddEditInboundModule::class,
    AddEditServerModule::class,
    AddEditClientModule::class,
    NetworkProvidesModule::class,
    StorageModule::class

])
interface AppComponent{
    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context): AppComponent
    }
    fun inject(activity: MainActivity)
    fun inject(fragment: MainFragment)

    fun inject(fragment: InboundsFragment)

    fun inject(fragment: ClientsFragment)

    fun inject(fragment: MetricsFragment)
    fun inject(fragment: SettingsFragment)
    fun inject(fragment: AddEditServerFragment)
    fun inject(fragment: AddEditInboundFragment)
    fun inject(fragment: AddEditClientFragment)
}