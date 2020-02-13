import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IArquivo, Arquivo } from 'app/shared/model/arquivo.model';
import { ArquivoService } from './arquivo.service';

@Component({
  selector: 'of-arquivo-update',
  templateUrl: './arquivo-update.component.html'
})
export class ArquivoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    caminhoDoArquivo: [null, [Validators.required]],
    extensao: [null, [Validators.required]],
    complexidade: []
  });

  constructor(protected arquivoService: ArquivoService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ arquivo }) => {
      this.updateForm(arquivo);
    });
  }

  updateForm(arquivo: IArquivo): void {
    this.editForm.patchValue({
      id: arquivo.id,
      caminhoDoArquivo: arquivo.caminhoDoArquivo,
      extensao: arquivo.extensao,
      complexidade: arquivo.complexidade
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const arquivo = this.createFromForm();
    if (arquivo.id !== undefined) {
      this.subscribeToSaveResponse(this.arquivoService.update(arquivo));
    } else {
      this.subscribeToSaveResponse(this.arquivoService.create(arquivo));
    }
  }

  private createFromForm(): IArquivo {
    return {
      ...new Arquivo(),
      id: this.editForm.get(['id'])!.value,
      caminhoDoArquivo: this.editForm.get(['caminhoDoArquivo'])!.value,
      extensao: this.editForm.get(['extensao'])!.value,
      complexidade: this.editForm.get(['complexidade'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IArquivo>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
