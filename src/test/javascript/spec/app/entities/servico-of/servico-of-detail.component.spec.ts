import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OfmanagerTestModule } from '../../../test.module';
import { ServicoOfDetailComponent } from 'app/entities/servico-of/servico-of-detail.component';
import { ServicoOf } from 'app/shared/model/servico-of.model';

describe('Component Tests', () => {
  describe('ServicoOf Management Detail Component', () => {
    let comp: ServicoOfDetailComponent;
    let fixture: ComponentFixture<ServicoOfDetailComponent>;
    const route = ({ data: of({ servicoOf: new ServicoOf(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [OfmanagerTestModule],
        declarations: [ServicoOfDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ServicoOfDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ServicoOfDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load servicoOf on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.servicoOf).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
