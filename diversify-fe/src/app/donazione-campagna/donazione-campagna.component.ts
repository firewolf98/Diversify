import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IPayPalConfig, ICreateOrderRequest } from 'ngx-paypal';
import { NgxPayPalModule } from 'ngx-paypal';
import { FormsModule } from '@angular/forms'; // Importa FormsModule per usare ngModel

@Component({
  selector: 'app-donazione-campagna',
  templateUrl: './donazione-campagna.component.html',
  styleUrls: ['./donazione-campagna.component.css'],
  standalone: true,
  imports: [NgxPayPalModule, CommonModule, FormsModule], // Aggiungi FormsModule qui
})
export class DonazioneCampagnaComponent {
  public payPalConfig?: IPayPalConfig;
  public donationAmount: number = 10.00; // Importo predefinito

  ngOnInit(): void {
    this.initConfig();
  }

  private initConfig(): void {
    this.updatePayPalConfig();
  }

  private updatePayPalConfig(): void {
    this.payPalConfig = {
      currency: 'EUR',
      clientId: 'test', // Mocked clientId per sandbox
      createOrderOnClient: (data) => <ICreateOrderRequest>{
        intent: 'CAPTURE',
        purchase_units: [
          {
            amount: {
              currency_code: 'EUR',
              value: this.donationAmount.toString(), // Usa l'importo inserito dall'utente
              breakdown: {
                item_total: {
                  currency_code: 'EUR',
                  value: this.donationAmount.toString(),
                },
              },
            },
            items: [
              {
                name: 'Donazione per la campagna',
                quantity: '1',
                category: 'DIGITAL_GOODS',
                unit_amount: {
                  currency_code: 'EUR',
                  value: this.donationAmount.toString(),
                },
              },
            ],
          },
        ],
      },
      advanced: {
        commit: 'true',
      },
      style: {
        label: 'paypal',
        layout: 'vertical',
      },
      onApprove: (data, actions) => {
        console.log('Pagamento approvato!', data);
      },
      onClientAuthorization: (data) => {
        console.log('Transazione completata', data);
      },
      onCancel: (data, actions) => {
        console.log('Pagamento annullato!', data);
      },
      onError: (err) => {
        console.log('Errore durante il pagamento!', err);
      },
    };
  }

  public updateDonationAmount(): void {
    this.updatePayPalConfig();
  }
}