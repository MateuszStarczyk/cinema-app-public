import { Injectable } from '@angular/core';
import Swal, { SweetAlertResult } from 'sweetalert2';

const ERROR_MESSAGE = 'Something went wrong, try again!';

@Injectable({
  providedIn: 'root',
})
export class AlertService {
  constructor() {}

  public showDefaultErrorMessage() {
    this.showCustomErrorMessage(ERROR_MESSAGE);
  }

  public showCustomErrorMessage(customMessage: string) {
    Swal.fire({
      icon: 'error',
      title: 'Error',
      text: customMessage,
    });
  }

  public showCustomWarningMessage(customMessage: string) {
    Swal.fire({
      icon: 'warning',
      title: 'Warning',
      text: customMessage,
    });
  }

  public showCustomSuccessMessage(customMessage: string) {
    Swal.fire({
      icon: 'success',
      title: 'Success',
      text: customMessage,
    });
  }

  public showYesNoDialog(message: string): Promise<SweetAlertResult> {
    return Swal.fire({
      title: message,
      icon: 'question',
      showCancelButton: true,
      cancelButtonText: 'No',
      confirmButtonText: 'Yes'
    });
  }

  public showLoader() {
    Swal.fire({
      title: 'Loading',
      allowEscapeKey: false,
      allowOutsideClick: false,
      showConfirmButton: false,
      onOpen: () => {
        Swal.showLoading();
      }
    });
  }

  public closeLoader() {
    Swal.close();
  }
}
